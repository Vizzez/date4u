package com.tutego.date4u.interfaces.controllers;

import com.tutego.date4u.core.profile.ProfileRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.Optional;

@RestController
public class StatisticRestController {

    private final ProfileRepository profiles;

    public StatisticRestController(ProfileRepository profiles) {
        this.profiles = profiles;

    }

    @RequestMapping("/api/stat/total")
    // long als Rückgabe ist auch in Ordnung
    public String totalNumberOfRegisteredUnicorns() {
        return String.valueOf(profiles.count());
    }

    @GetMapping("/api/stat/last-seen")
    public LastSeenStatistics lastSeenStatistics(@RequestParam("start") Optional<String> start, @RequestParam("end") Optional<String> end) {
        YearMonth startYM = start.map(YearMonth::parse).orElse(YearMonth.now().minusYears(20));
        YearMonth endYM = end.map(YearMonth::parse).orElse(YearMonth.now());
      System.out.println(endYM);
        return new LastSeenStatistics(profiles.findMonthlyProfileCount().stream().map(
                tuple -> {
                    return new LastSeenStatistics.Data(YearMonth.of(Integer.parseInt(tuple.get("y").toString()),
                            Integer.parseInt(tuple.get("m").toString())),
                            Integer.parseInt(tuple.get("count").toString()));
                }).filter(p -> p.yearMonth.isBefore(endYM) && p.yearMonth.isAfter(startYM)).toList());
    }


}