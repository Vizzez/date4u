package com.tutego.date4u.core.controllers;


import com.tutego.date4u.core.PhotoService;
import com.tutego.date4u.core.configuration.ActiveUserStore;
import com.tutego.date4u.core.configuration.UnicornUser;
import com.tutego.date4u.core.entities.Photo;
import com.tutego.date4u.core.entities.Profile;
import com.tutego.date4u.core.formData.FilterSearchFormData;
import com.tutego.date4u.core.formData.ProfileFormData;
import com.tutego.date4u.core.repositories.PhotoRepository;
import com.tutego.date4u.core.repositories.ProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Controller
public class Date4uWebController {
    private static final int PAGINATION_SIZE = 6;
    private final ProfileRepository profileRepo;
    private final EntityManager entityManager;
    private final PhotoRepository photoRepo;
    private final PhotoService photoService;
    private final ActiveUserStore activeUserStore;


    public Date4uWebController(PhotoService photoService, ProfileRepository profileRepo, PhotoRepository photoRepo,
                               EntityManager em, ActiveUserStore activeUserStore) {
        this.profileRepo = profileRepo;
        this.entityManager = em;
        this.photoRepo = photoRepo;
        this.photoService = photoService;
        this.activeUserStore = activeUserStore;
    }

    @RequestMapping("/")
    public String indexPage(Authentication a, Model model) {

        if (a != null) {
            UnicornUser user = (UnicornUser) a.getPrincipal();
            model.addAttribute("name", user.getName());
        } else
            model.addAttribute("name", "");

        model.addAttribute("userOnlineCount", activeUserStore.getUsers()
                .size());
        return "index";
    }

    @GetMapping({"/profile", "/profile/{id}"})
    public String myProfile(Authentication a, Model model, @PathVariable("id") Optional<Long> id) {

        ProfileFormData profileFormData;
        boolean myProfile = false;
        boolean liked = false;
        int likesCount = 0;
        Long likeeID;
        Optional<Profile> profileToShowOpt;

        UnicornUser user = (UnicornUser) a.getPrincipal();

        if (id.isEmpty()) {
            profileToShowOpt = profileRepo.findById(user.getpId());
            likeeID = null;
            myProfile = true;

        } else {
            likeeID = id.get();
            profileToShowOpt = profileRepo.findById(id.get());
            Optional<Profile> currentProfile = profileRepo.findById(user.getpId());
            if (currentProfile.isPresent()) {
                Set<Profile> profilesThatILike = currentProfile.get()
                        .getProfilesThatILike();
                for (Profile profile : profilesThatILike) {
                    if (id.get()
                            .equals(profile.getId())) {
                        liked = true;
                        break;
                    }
                }
            }
            if (id.get().equals(user.getpId())) {
                myProfile = true;
            }
        }

        if (profileToShowOpt.isPresent()) {
            Profile profile = profileToShowOpt.get();
            profileFormData = new ProfileFormData(profile);
            likesCount = profile.getProfilesThatLikeMe().size();
        } else {
            profileFormData = new ProfileFormData();
        }


        model.addAttribute("profileFormData", profileFormData);
        model.addAttribute("liked", liked);
        model.addAttribute("likeeId", likeeID);
        model.addAttribute("myProfile", myProfile);
        model.addAttribute("likesCount", likesCount);

        return "profile";

    }

    @PostMapping("/profile")
    public String updateProfile(Authentication auth, @Valid ProfileFormData profileFD, BindingResult bindingResult,
                                Model model) {
        UnicornUser user = (UnicornUser) auth.getPrincipal();
        Optional<Profile> profile = profileRepo.findById(user.getpId());
        model.addAttribute("myProfile", true);

        if (profile.isPresent()) {
            Profile updateP = profile.get();
            profileFD.updateDetails(updateP);
            model.addAttribute("profileFormData", profileFD);
            if (bindingResult.hasErrors()) {
                return "profile";
            }

            updateP.setDescription(profileFD.getDescription());
            updateP.setBirthdate(profileFD.getBirthdate());
            updateP.setGender(profileFD.getGender());
            updateP.setAttractedToGender(profileFD.getAttractedToGender());
            updateP.setHornlength(profileFD.getHornlength());
            updateP.setNickname(profileFD.getNickname());
            updateP.setDescription(profileFD.getDescription());
            profileRepo.save(updateP);
            profileRepo.flush();
            model.addAttribute("success", true);
        }
        return "profile";

    }

    @PostMapping("/updateProfilePhoto")
    public String updateProfilePhoto(@RequestParam("imgNameInput") String imgName,
                                     @RequestParam("action") String action, Authentication auth) {
        UnicornUser user = (UnicornUser) auth.getPrincipal();
        Optional<Photo> optPhoto = photoRepo.findPhotoByIdAndName(user.getpId(), imgName);
        if (optPhoto.isPresent()) {
            Photo photo = optPhoto.get();
            if (action.equals("delete")) {
                photoRepo.delete(photo);

            } else if (action.equals("update")) {
                Optional<Photo> profilePhoto = photoRepo.findProfilePhotoById(user.getpId());
                if (profilePhoto.isPresent()) {
                    profilePhoto.get()
                            .setProfilePhoto(false);
                    photoRepo.save(profilePhoto.get());
                }
                photo.setProfilePhoto(true);
                photoRepo.save(photo);
            }
            photoRepo.flush();

        }
        return "redirect:/profile";
    }


    @GetMapping(value = {"/search", "/search/{page}"})
    public String searchPage(Model model, @PathVariable("page") Optional<Integer> page, HttpSession session) {

        Optional<Object> fsfdOpt = Optional.ofNullable(session.getAttribute("fsfd"));

        FilterSearchFormData fsfd = fsfdOpt.map(FilterSearchFormData.class::cast)
                .orElseGet(FilterSearchFormData::new);
        PageRequest pageRequest;
        pageRequest = page.map(integer -> PageRequest.of(integer - 1, PAGINATION_SIZE))
                .orElseGet(() -> PageRequest.ofSize(PAGINATION_SIZE));


        List<ProfileFormData> allPFD = new ArrayList<>();

        for (Profile profile : (List<Profile>) searchFilter(fsfd, pageRequest, true).toList()) {
            allPFD.add(new ProfileFormData(profile));
        }
        model.addAttribute("filter", fsfd);
        model.addAttribute("profiles", allPFD);
        Long numOfElements = (Long) searchFilter(fsfd, pageRequest, false).toList()
                .get(0);

        long totalPages;
        if (numOfElements % PAGINATION_SIZE == 0)
            totalPages = numOfElements / PAGINATION_SIZE;
        else
            totalPages = numOfElements / PAGINATION_SIZE + 1;

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("numOfElements", numOfElements);
        model.addAttribute("pageOffset", pageRequest.getOffset());
        model.addAttribute("pageSize", pageRequest.getPageSize());
        return "search";
    }


    @PostMapping("/search")
    public String searchFilter(@ModelAttribute FilterSearchFormData filter, HttpSession session) {


        if (filter.getMinHornlength() > filter.getMaxHornlength()) {
            int tempMH = filter.getMinHornlength();
            filter.setMinHornlength(filter.getMaxHornlength());
            filter.setMaxHornlength(tempMH);
        }
        if (filter.getMaxHornlength() > 100) {
            filter.setMaxHornlength(100);
        }
        if (filter.getMinHornlength() < 0) {
            filter.setMinHornlength(0);
        }
        if (filter.getMinAge() < 18)
            filter.setMinAge(18);
        if (filter.getMaxAge() < 18)
            filter.setMaxAge(18);
        if (filter.getMinAge() > filter.getMaxAge()) {
            int tempMA = filter.getMinAge();
            filter.setMinAge(filter.getMaxAge());
            filter.setMaxAge(tempMA);
        }
        session.setAttribute("fsfd", filter);

        return "redirect:/search";
    }


    @PostMapping(path = "/addPhoto")
    public String uploadPhoto(Authentication auth, @RequestParam("imageUpload") MultipartFile mpFile) throws IOException {
        UnicornUser user = (UnicornUser) auth.getPrincipal();
        Optional<Profile> optProfile = profileRepo.findById(user.getpId());

        if (optProfile.isEmpty())
            return "redirect:/profile";
        String result = photoService.upload(mpFile.getBytes());
        Profile profile = optProfile.get();
        Photo photo = new Photo(null, profile, result, false);
        profile.add(photo);
        profileRepo.save(profile);
        return "redirect:/profile";
    }

    @GetMapping(path = "/error")
    public String showErrorPage() {
        return "error";
    }

    @PostMapping(path = "/addLike")
    public String addLike(Authentication auth, @RequestParam("likeeId") long likeePId) {
        UnicornUser user = (UnicornUser) auth.getPrincipal();
        Optional<Profile> currentProfileOpt = profileRepo.findById(user.getpId());
        if (currentProfileOpt.isPresent()) {

            Profile currentProfile = currentProfileOpt.get();
            Optional<Profile> likeProfileOpt = profileRepo.findById(likeePId);
            if (likeProfileOpt.isPresent()) {
                Profile likeProfile = likeProfileOpt.get();
                Set<Profile> likedProfiles = currentProfile.getProfilesThatILike();
                if (likedProfiles.contains(likeProfile)) {
                    likedProfiles.remove(likeProfile);
                } else {

                    currentProfile.getProfilesThatILike()
                            .add(likeProfile);
                }

                profileRepo.save(currentProfile);
                profileRepo.flush();
            }

        }

        return "redirect:/profile/" + likeePId;
    }

    @GetMapping("/loggedUsers")
    public String getLoggedUsers(Model model) {
        model.addAttribute("users", activeUserStore.getUsers());
        return "Users";
    }

    public Stream searchFilter(FilterSearchFormData fsfd, Pageable pageable, boolean b) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<Profile> profile = criteriaQuery.from(Profile.class);

        Predicate genderPred = fsfd.getGender() != 0 ?
                criteriaBuilder.equal(profile.get("gender"), fsfd.getGender()) : criteriaBuilder.and();

        Predicate genderAttrPred = fsfd.getAttractedToGender() != 0 ? criteriaBuilder.equal(profile.get(
                "attractedToGender"), fsfd.getAttractedToGender()) : criteriaBuilder.and();

        Predicate hornlengthPred = criteriaBuilder.between(profile.get("hornlength"), fsfd.getMinHornlength(),
                fsfd.getMaxHornlength());
        Predicate birthdatePred = criteriaBuilder.between(profile.get("birthdate"), LocalDate.now()
                .minusYears(fsfd.getMaxAge() + 1L)
                .plusDays(1), LocalDate.now()
                .minusYears(fsfd.getMinAge()));


        if (b) {
            criteriaQuery.where(genderPred, genderAttrPred, hornlengthPred, birthdatePred)
                    .orderBy(criteriaBuilder.desc(profile.get("lastseen")));

            Query query = entityManager.createQuery(criteriaQuery)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());
            return query.getResultList()
                    .stream();
        } else {
            criteriaQuery.select(criteriaBuilder.count(profile));
            criteriaQuery.where(genderPred, genderAttrPred, hornlengthPred, birthdatePred);

            Query query = entityManager.createQuery(criteriaQuery);
            Long count = (Long) query.getSingleResult();
            return Stream.of(count);
        }

    }


}