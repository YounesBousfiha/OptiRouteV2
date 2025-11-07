package com.optiroute.optiroute.infrastructure.seeder;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
// @Profile({"dev"})
@RequiredArgsConstructor
public class MasterSeeder implements CommandLineRunner {

    private final List<Seeder> seeders;

    @Override
    public void run(String... args) throws Exception {
        seeders.forEach(Seeder::seed);
    }
}
