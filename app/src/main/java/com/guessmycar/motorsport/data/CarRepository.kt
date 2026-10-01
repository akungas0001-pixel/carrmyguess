package com.guessmycar.motorsport.data

object CarRepository {

    val regions = listOf(
        Region(
            id = "cn", name = "China", code = "CN", flag = "🇨🇳",
            title = "CHINESE INNOVATION", subtitle = "NEW ENERGY FRONTIER", totalCars = 20,
            description = "From battery-swap flagships to tank-tough off-roaders — explore China's fast-rising auto industry."
        ),
        Region(
            id = "us", name = "United States", code = "US", flag = "🇺🇸",
            title = "AMERICAN MUSCLE", subtitle = "DETROIT HORSEPOWER", totalCars = 20,
            description = "Big-displacement V8 thunder, supercharged muscle cars, and quarter-mile drag strip icons."
        ),
        Region(
            id = "jp", name = "Japan", code = "JP", flag = "🇯🇵",
            title = "JAPANESE ENGINEERING", subtitle = "AUTOMOTIVE VISUAL TRIVIA", totalCars = 20,
            description = "Master legendary turbocharged straight-sixes, twin-rotor machines, and AWD rally titans."
        ),
        Region(
            id = "de", name = "Germany", code = "DE", flag = "🇩🇪",
            title = "GERMAN PRECISION", subtitle = "NÜRBURGRING LEGENDS", totalCars = 20,
            description = "High-revving flat-sixes, twin-turbo V8 autobahn dominators, and precision DTM track weaponry."
        ),
        Region(
            id = "kr", name = "South Korea", code = "KR", flag = "🇰🇷",
            title = "KOREAN AMBITION", subtitle = "RAPID PERFORMANCE RISE", totalCars = 20,
            description = "From 800V electric platforms to twin-turbo grand tourers — Korea built a performance pedigree fast."
        ),
        Region(
            id = "it", name = "Italy", code = "IT", flag = "🇮🇹",
            title = "ITALIAN PASSION", subtitle = "MARANELLO & SANT'AGATA", totalCars = 20,
            description = "Screaming naturally aspirated engines, wedge-era icons, and hand-built hypercar artistry."
        ),
        Region(
            id = "fr", name = "France", code = "FR", flag = "🇫🇷",
            title = "FRENCH ENGINEERING", subtitle = "AVANT-GARDE AUTOMOBILES", totalCars = 20,
            description = "Quirky hydropneumatic classics, pocket-rocket hot hatches, and quad-turbo W16 hypercars."
        ),
        Region(
            id = "gb", name = "United Kingdom", code = "GB", flag = "🇬🇧",
            title = "BRITISH HERITAGE", subtitle = "CLASSIC & CRAFT", totalCars = 20,
            description = "Hand-built grand tourers, go-kart handling icons, and Woking supercar engineering."
        )
    )

    private fun car(
        id: Int,
        regionId: String,
        manufacturer: String,
        model: String,
        generation: String,
        year: Int,
        difficulty: String,
        description: String
    ): Car {
        val regionName = regions.first { it.id == regionId }.name
        val imageRes = listOf(manufacturer, model, generation)
            .filter { it.isNotBlank() }
            .joinToString("_") { it.lowercase().replace(Regex("[^a-z0-9]+"), "_").trim('_') } + ".png"
        return Car(
            id = id,
            regionId = regionId,
            region = regionName,
            country = regionName,
            manufacturer = manufacturer,
            model = model,
            generation = generation,
            productionYear = year,
            imageResource = imageRes,
            difficulty = difficulty,
            description = description
        )
    }

    val cars: List<Car> = listOf(
        // ---- China (1-20) ----
        car(1, "cn", "BYD", "Yangwang U9", "", 2023, "Hard", "BYD's Yangwang sub-brand electric hypercar capable of tank-turns on its own wheels."),
        car(2, "cn", "BYD", "Seal", "", 2022, "Medium", "A mid-size electric sport sedan built on BYD's e-Platform 3.0."),
        car(3, "cn", "BYD", "Han", "", 2020, "Medium", "BYD's flagship electric sedan named after the Han dynasty."),
        car(4, "cn", "BYD", "Tang", "", 2018, "Medium", "A three-row plug-in hybrid SUV from BYD's Dynasty series."),
        car(5, "cn", "BYD", "Dolphin", "", 2021, "Medium", "A compact electric hatchback aimed at China's urban EV market."),
        car(6, "cn", "NIO", "EP9", "", 2016, "Hard", "A limited-run electric hypercar built to showcase NIO's performance technology."),
        car(7, "cn", "NIO", "ET7", "", 2022, "Medium", "NIO's flagship electric sedan with swappable battery technology."),
        car(8, "cn", "NIO", "EC7", "", 2023, "Hard", "A coupe-SUV electric flagship from NIO."),
        car(9, "cn", "XPeng", "P7", "", 2020, "Medium", "A sleek electric sedan known for its long range and ADAS tech."),
        car(10, "cn", "XPeng", "G9", "", 2023, "Medium", "XPeng's flagship electric SUV with 800V fast-charging architecture."),
        car(11, "cn", "Li Auto", "L9", "", 2022, "Medium", "A large extended-range electric SUV from Li Auto."),
        car(12, "cn", "Zeekr", "001", "", 2021, "Medium", "A shooting-brake electric performance car from Geely's Zeekr brand."),
        car(13, "cn", "Zeekr", "X", "", 2023, "Hard", "A compact electric crossover from Zeekr."),
        car(14, "cn", "Hongqi", "H9", "", 2020, "Medium", "Hongqi's modern flagship sedan, successor to decades of state limousines."),
        car(15, "cn", "MG", "Cyberster", "", 2023, "Medium", "A modern electric roadster reviving MG's open-top sports car heritage."),
        car(16, "cn", "MG", "4 EV", "", 2022, "Medium", "A compact electric hatchback built on MG's dedicated EV platform."),
        car(17, "cn", "Aion", "Hyper SSR", "", 2023, "Hard", "GAC Aion's electric supercar with scissor doors."),
        car(18, "cn", "Wuling", "Hongguang Mini EV", "", 2020, "Easy", "A tiny budget EV that became one of China's best-selling cars."),
        car(19, "cn", "Chery", "Omoda 5", "", 2022, "Hard", "A compact crossover from Chery's Omoda sub-brand."),
        car(20, "cn", "Geely", "Xingyue L", "", 2021, "Medium", "Geely's flagship turbocharged SUV."),

        // ---- United States (21-40) ----
        car(21, "us", "Ford", "Mustang", "GT", 2015, "Easy", "A V8-powered pony car and American muscle icon."),
        car(22, "us", "Ford", "Mustang", "Shelby GT500", 2020, "Easy", "A supercharged V8 Mustang built for the track."),
        car(23, "us", "Ford", "GT", "", 2017, "Easy", "A twin-turbo V6 supercar built to honor the GT40's Le Mans legacy."),
        car(24, "us", "Ford", "Focus", "RS", 2016, "Medium", "An all-wheel-drive turbocharged hot hatch with a drift mode."),
        car(25, "us", "Chevrolet", "Camaro", "ZL1", 2017, "Easy", "A supercharged V8 Camaro built to rival the Mustang GT500."),
        car(26, "us", "Chevrolet", "Corvette", "C8", 2020, "Easy", "The first mid-engine Corvette in the nameplate's history."),
        car(27, "us", "Chevrolet", "Corvette", "C7 Z06", 2015, "Medium", "A supercharged front-engine Corvette track special."),
        car(28, "us", "Chevrolet", "Impala", "SS", 1996, "Hard", "A sleeper full-size sedan with a Corvette-sourced LT1 V8."),
        car(29, "us", "Dodge", "Challenger", "Hellcat", 2015, "Easy", "A supercharged HEMI V8 muscle car with 707 horsepower."),
        car(30, "us", "Dodge", "Charger", "Hellcat", 2015, "Easy", "A supercharged four-door muscle sedan."),
        car(31, "us", "Dodge", "Viper", "ACR", 2016, "Medium", "A naturally aspirated V10 track special with extreme aerodynamics."),
        car(32, "us", "Tesla", "Model S", "Plaid", 2021, "Easy", "A tri-motor electric sedan capable of sub-two-second 0-60 runs."),
        car(33, "us", "Tesla", "Roadster", "", 2008, "Medium", "Tesla's first production car, based on a Lotus Elise chassis."),
        car(34, "us", "Cadillac", "CTS-V", "", 2009, "Medium", "A supercharged V8 luxury sport sedan from Cadillac's V-Series."),
        car(35, "us", "Cadillac", "CT5-V", "Blackwing", 2022, "Medium", "Cadillac's last manual-transmission super sedan."),
        car(36, "us", "Jeep", "Grand Cherokee", "Trackhawk", 2018, "Medium", "A supercharged HEMI SUV with Hellcat power."),
        car(37, "us", "Hennessey", "Venom GT", "", 2012, "Hard", "A limited-production American hypercar built on a Lotus Exige chassis."),
        car(38, "us", "Saleen", "S7", "", 2000, "Hard", "A hand-built American supercar with a twin-turbo V8."),
        car(39, "us", "SSC", "Tuatara", "", 2020, "Hard", "An American hypercar built to chase top-speed records."),
        car(40, "us", "Shelby", "Cobra", "427", 1965, "Medium", "A big-block V8 roadster and classic American sports car icon."),

        // ---- Japan (41-60) ----
        car(41, "jp", "Toyota", "Supra", "MK4", 1993, "Easy", "Famous for its legendary 2JZ-GTE engine, capable of huge power with tuning."),
        car(42, "jp", "Toyota", "Supra", "A90", 2019, "Easy", "The revived Supra, co-developed with BMW on a shared platform."),
        car(43, "jp", "Toyota", "GR86", "", 2021, "Medium", "A lightweight rear-drive sports coupe co-developed with Subaru."),
        car(44, "jp", "Toyota", "AE86", "", 1983, "Medium", "A lightweight rear-drive coupe beloved in drift and touge culture."),
        car(45, "jp", "Toyota", "Celica", "GT-Four", 1986, "Hard", "A turbocharged all-wheel-drive rally homologation special."),
        car(46, "jp", "Toyota", "MR2", "SW20", 1989, "Medium", "A mid-engine sports car known for its sharp handling."),
        car(47, "jp", "Nissan", "GT-R", "R32", 1989, "Medium", "The first 'Godzilla' GT-R, dominant in Group A touring car racing."),
        car(48, "jp", "Nissan", "GT-R", "R34", 1999, "Easy", "An iconic twin-turbo AWD sports car nicknamed Godzilla."),
        car(49, "jp", "Nissan", "GT-R", "R35", 2007, "Easy", "A modern AWD supercar-killer with a hand-built twin-turbo V6."),
        car(50, "jp", "Nissan", "370Z", "", 2008, "Medium", "A front-engine, rear-drive sports coupe in Nissan's Z lineage."),
        car(51, "jp", "Mazda", "RX-7", "FD", 1992, "Easy", "A twin-turbo rotary-powered sports car with near-perfect weight balance."),
        car(52, "jp", "Mazda", "RX-8", "", 2003, "Medium", "A rotary-powered sports car with rear-hinged half-doors."),
        car(53, "jp", "Honda", "NSX", "NA1", 1990, "Easy", "A mid-engine supercar chassis-tuned with input from Ayrton Senna."),
        car(54, "jp", "Honda", "Civic Type R", "FK8", 2017, "Medium", "A turbocharged front-drive hot hatch and former Nürburgring record holder."),
        car(55, "jp", "Honda", "S2000", "", 1999, "Medium", "A high-revving naturally aspirated roadster with a 9,000 rpm redline."),
        car(56, "jp", "Subaru", "Impreza WRX", "STI", 2004, "Medium", "A turbocharged, symmetrical-AWD rally-bred sports sedan."),
        car(57, "jp", "Subaru", "BRZ", "", 2012, "Medium", "A lightweight rear-drive sports coupe co-developed with Toyota."),
        car(58, "jp", "Mitsubishi", "Lancer Evolution", "IX", 2005, "Medium", "A turbocharged AWD rally homologation sedan."),
        car(59, "jp", "Mitsubishi", "Eclipse", "GSX", 1990, "Hard", "A turbocharged all-wheel-drive sport compact."),
        car(60, "jp", "Lexus", "LFA", "", 2010, "Medium", "A hand-built carbon-fiber supercar with a screaming naturally aspirated V10."),

        // ---- Germany (61-80) ----
        car(61, "de", "BMW", "M3", "E30", 1986, "Medium", "The original M3, built to homologate BMW for touring car racing."),
        car(62, "de", "BMW", "M3", "E46", 2000, "Easy", "Prized for its naturally aspirated inline-six and sharp chassis balance."),
        car(63, "de", "BMW", "M4", "G82", 2021, "Easy", "The coupe counterpart to the M3, with a twin-turbo inline-six."),
        car(64, "de", "BMW", "M5", "F90", 2017, "Medium", "A twin-turbo V8 super sedan with all-wheel drive."),
        car(65, "de", "BMW", "i8", "", 2014, "Medium", "A plug-in hybrid sports car with futuristic scissor doors."),
        car(66, "de", "Mercedes", "AMG GT", "", 2015, "Medium", "A front-mid-engine sports car hand-built by AMG."),
        car(67, "de", "Mercedes", "C63", "AMG", 2008, "Medium", "A naturally aspirated V8 performance version of the C-Class."),
        car(68, "de", "Mercedes", "SLS", "AMG", 2010, "Easy", "A modern gullwing-door supercar inspired by the 300 SL."),
        car(69, "de", "Mercedes", "CLK", "GTR", 1997, "Hard", "A rare GT1 homologation special built for Le Mans racing."),
        car(70, "de", "Audi", "R8", "", 2006, "Easy", "A mid-engine supercar that shares its V10 with the Lamborghini Huracan."),
        car(71, "de", "Audi", "RS6", "", 2020, "Medium", "A twin-turbo V8 performance wagon with Quattro all-wheel drive."),
        car(72, "de", "Audi", "TT", "RS", 2009, "Medium", "A turbocharged five-cylinder compact sports car."),
        car(73, "de", "Audi", "RS7", "", 2013, "Medium", "A twin-turbo V8 four-door performance fastback."),
        car(74, "de", "Porsche", "911", "Carrera", 2019, "Easy", "The latest generation of Porsche's iconic rear-engine sports car."),
        car(75, "de", "Porsche", "911", "GT3", 2021, "Easy", "A track-focused, naturally aspirated flat-six 911."),
        car(76, "de", "Porsche", "918", "Spyder", 2013, "Easy", "A plug-in hybrid hypercar with a naturally aspirated V8."),
        car(77, "de", "Porsche", "Cayman", "GT4", 2015, "Medium", "A mid-engine track special built on the Cayman platform."),
        car(78, "de", "Volkswagen", "Golf", "GTI", 2013, "Easy", "The hatchback that invented the hot-hatch formula in 1976."),
        car(79, "de", "Volkswagen", "Golf", "R", 2015, "Medium", "The all-wheel-drive, range-topping performance Golf."),
        car(80, "de", "Volkswagen", "Scirocco", "R", 2009, "Medium", "A turbocharged front-drive coupe based on the Golf platform."),

        // ---- South Korea (81-100) ----
        car(81, "kr", "Hyundai", "N Vision 74", "", 2022, "Hard", "A retro-styled hydrogen hybrid concept inspired by the 1974 Pony Coupe."),
        car(82, "kr", "Hyundai", "Elantra", "N", 2021, "Medium", "A turbocharged performance version of Hyundai's compact sedan."),
        car(83, "kr", "Hyundai", "i30", "N", 2017, "Medium", "Hyundai's first N-badged hot hatch, tuned on the Nürburgring."),
        car(84, "kr", "Hyundai", "Ioniq 5", "N", 2023, "Easy", "A high-performance electric crossover with simulated gear shifts."),
        car(85, "kr", "Genesis", "G70", "", 2018, "Medium", "A turbocharged rear-drive sport sedan benchmarked against German rivals."),
        car(86, "kr", "Genesis", "G80", "", 2020, "Medium", "Genesis' mid-size luxury sedan flagship."),
        car(87, "kr", "Genesis", "GV80", "Coupe", 2023, "Medium", "A coupe-styled version of Genesis' flagship luxury SUV."),
        car(88, "kr", "Genesis", "X", "Concept", 2021, "Hard", "A grand touring concept previewing Genesis' electric design language."),
        car(89, "kr", "Kia", "Stinger", "GT", 2017, "Medium", "A twin-turbo V6 rear-drive liftback halo car for Kia."),
        car(90, "kr", "Kia", "EV6", "GT", 2022, "Medium", "A high-performance dual-motor version of Kia's E-GMP electric crossover."),
        car(91, "kr", "Kia", "K5", "GT", 2020, "Hard", "A turbocharged performance trim of Kia's mid-size sedan."),
        car(92, "kr", "Hyundai", "Veloster", "N", 2019, "Medium", "A turbocharged hot hatch with an unusual asymmetric door layout."),
        car(93, "kr", "Hyundai", "Tiburon", "", 1996, "Hard", "A front-drive sport coupe sold globally through the early 2000s."),
        car(94, "kr", "Hyundai", "Genesis Coupe", "", 2008, "Medium", "A rear-drive sport coupe that predated the standalone Genesis brand."),
        car(95, "kr", "Kia", "Forte", "GT", 2019, "Hard", "A turbocharged performance trim of Kia's compact sedan."),
        car(96, "kr", "Kia", "K8", "", 2021, "Hard", "Kia's full-size flagship sedan, successor to the Cadenza."),
        car(97, "kr", "Genesis", "GV60", "", 2022, "Medium", "Genesis' first dedicated electric crossover."),
        car(98, "kr", "Kia", "Telluride", "", 2019, "Medium", "A three-row flagship SUV praised for its design and value."),
        car(99, "kr", "Hyundai", "Sonata", "N Line", 2020, "Medium", "A turbocharged sport trim of Hyundai's mid-size sedan."),
        car(100, "kr", "Genesis", "X Speedium Coupe", "", 2022, "Hard", "A grand touring concept previewing a future electric coupe."),

        // ---- Italy (101-120) ----
        car(101, "it", "Ferrari", "F40", "", 1987, "Easy", "The last car personally approved by Enzo Ferrari, built with no frills."),
        car(102, "it", "Ferrari", "F50", "", 1995, "Medium", "A V12 supercar derived from Ferrari's Formula 1 program."),
        car(103, "it", "Ferrari", "Enzo", "", 2002, "Easy", "A V12 halo supercar named after Ferrari's founder."),
        car(104, "it", "Ferrari", "LaFerrari", "", 2013, "Easy", "Ferrari's first hybrid hypercar, pairing a V12 with an electric motor."),
        car(105, "it", "Ferrari", "488", "GTB", 2015, "Medium", "Ferrari's return to turbocharging for its mid-engine V8 line."),
        car(106, "it", "Ferrari", "SF90", "Stradale", 2019, "Medium", "Ferrari's first plug-in hybrid production supercar."),
        car(107, "it", "Lamborghini", "Gallardo", "", 2003, "Medium", "Lamborghini's best-selling model, powered by a naturally aspirated V10."),
        car(108, "it", "Lamborghini", "Huracan", "", 2014, "Easy", "A naturally aspirated V10 supercar that succeeded the Gallardo."),
        car(109, "it", "Lamborghini", "Aventador", "", 2011, "Easy", "A naturally aspirated V12 flagship supercar."),
        car(110, "it", "Lamborghini", "Revuelto", "", 2023, "Medium", "Lamborghini's first V12 plug-in hybrid flagship."),
        car(111, "it", "Lamborghini", "Diablo", "", 1990, "Medium", "A wedge-shaped V12 supercar from Lamborghini's 1990s lineup."),
        car(112, "it", "Pagani", "Zonda", "", 1999, "Medium", "A hand-built hypercar powered by a Mercedes-AMG V12."),
        car(113, "it", "Pagani", "Huayra", "", 2011, "Medium", "A hypercar named after a Quechua wind god, with active aero flaps."),
        car(114, "it", "Maserati", "MC20", "", 2020, "Medium", "A supercar powered by Maserati's first fully in-house engine in decades."),
        car(115, "it", "Maserati", "GranTurismo", "", 2007, "Medium", "A V8-powered grand tourer with Pininfarina styling."),
        car(116, "it", "Alfa Romeo", "Giulia", "Quadrifoglio", 2016, "Medium", "A twin-turbo V6 sport sedan developed with input from Ferrari engineers."),
        car(117, "it", "Alfa Romeo", "4C", "", 2013, "Medium", "A lightweight carbon-tub sports car with a turbocharged four-cylinder."),
        car(118, "it", "Lancia", "Delta", "Integrale", 1987, "Hard", "A turbocharged AWD rally homologation legend."),
        car(119, "it", "Fiat", "Abarth 595", "", 2008, "Medium", "A turbocharged, performance-tuned version of the Fiat 500."),
        car(120, "it", "Ferrari", "812", "Superfast", 2017, "Medium", "A front-engine V12 grand tourer, among the last naturally aspirated Ferraris."),

        // ---- France (121-140) ----
        car(121, "fr", "Bugatti", "Chiron", "", 2016, "Easy", "A quad-turbo W16 hypercar and successor to the Veyron."),
        car(122, "fr", "Bugatti", "Veyron", "", 2005, "Easy", "The car that redefined production-car top speed in the 2000s."),
        car(123, "fr", "Bugatti", "Divo", "", 2018, "Hard", "A track-focused, limited-run version of the Chiron."),
        car(124, "fr", "Alpine", "A110", "", 2017, "Medium", "A lightweight all-aluminum sports coupe reviving a 1960s rally name."),
        car(125, "fr", "Renault", "Megane", "RS", 2018, "Medium", "A turbocharged hot hatch with four-wheel steering."),
        car(126, "fr", "Renault", "Clio", "RS", 2006, "Medium", "A naturally aspirated hot hatch celebrated for its chassis balance."),
        car(127, "fr", "Renault", "5", "Turbo", 1980, "Hard", "A mid-engine rally homologation special based on the Renault 5."),
        car(128, "fr", "Renault", "Sport Spider", "", 1996, "Hard", "A lightweight, track-focused roadster with no windshield in early versions."),
        car(129, "fr", "Peugeot", "205", "GTI", 1984, "Medium", "Regarded as the gold standard for 1980s hot hatch handling."),
        car(130, "fr", "Peugeot", "RCZ", "", 2010, "Medium", "A double-bubble roofed coupe based on the 308 platform."),
        car(131, "fr", "Peugeot", "508", "PSE", 2020, "Hard", "A plug-in hybrid performance flagship for Peugeot."),
        car(132, "fr", "Citroen", "DS3", "Racing", 2011, "Hard", "A turbocharged, limited-run hot hatch tuned with Citroen Racing input."),
        car(133, "fr", "Citroen", "GT", "", 2008, "Hard", "A futuristic supercar concept created for the Gran Turismo video game."),
        car(134, "fr", "Venturi", "Atlantique", "", 1991, "Hard", "A rare French-built turbocharged sports car."),
        car(135, "fr", "Venturi", "Fetish", "", 2004, "Hard", "An early French electric sports car from boutique maker Venturi."),
        car(136, "fr", "DS", "9", "", 2021, "Hard", "DS Automobiles' flagship luxury sedan."),
        car(137, "fr", "Renault", "Alpine GTA", "", 1985, "Hard", "A rear-engine French sports car, forerunner to the modern Alpine brand."),
        car(138, "fr", "Peugeot", "208", "GTi", 2013, "Medium", "A turbocharged modern revival of Peugeot's GTI hot hatch heritage."),
        car(139, "fr", "Bugatti", "Bolide", "", 2020, "Hard", "A track-only, ultra-lightweight hypercar concept."),
        car(140, "fr", "Alpine", "A310", "", 1971, "Hard", "A fiberglass-bodied rear-engine sports car from classic-era Alpine."),

        // ---- United Kingdom (141-160) ----
        car(141, "gb", "McLaren", "F1", "", 1992, "Easy", "A center-seat V12 supercar that held the production top-speed record for years."),
        car(142, "gb", "McLaren", "P1", "", 2013, "Easy", "A hybrid hypercar built alongside the LaFerrari and 918 as part of the 'Holy Trinity'."),
        car(143, "gb", "McLaren", "720S", "", 2017, "Easy", "A twin-turbo V8 supercar with distinctive 'eye socket' headlight ducts."),
        car(144, "gb", "McLaren", "Artura", "", 2021, "Medium", "McLaren's first series-production plug-in hybrid supercar."),
        car(145, "gb", "Aston Martin", "DB11", "", 2016, "Medium", "A grand tourer that launched a new design era for Aston Martin."),
        car(146, "gb", "Aston Martin", "Valkyrie", "", 2021, "Medium", "A Formula 1-inspired hypercar co-developed with Red Bull Racing."),
        car(147, "gb", "Aston Martin", "Vantage", "", 2018, "Medium", "A twin-turbo V8 sports car in Aston Martin's lineup."),
        car(148, "gb", "Bentley", "Continental", "GT", 2018, "Medium", "A twin-turbo W12 luxury grand tourer."),
        car(149, "gb", "Bentley", "Flying Spur", "", 2019, "Hard", "A four-door luxury sedan built on the Continental platform."),
        car(150, "gb", "Rolls-Royce", "Phantom", "", 2017, "Medium", "Rolls-Royce's ultra-luxury flagship sedan."),
        car(151, "gb", "Rolls-Royce", "Ghost", "", 2020, "Medium", "A more understated, driver-focused Rolls-Royce luxury sedan."),
        car(152, "gb", "Lotus", "Evija", "", 2021, "Hard", "Lotus' first electric hypercar, with four motors and over 1,900 horsepower."),
        car(153, "gb", "Lotus", "Emira", "", 2021, "Medium", "Lotus' last combustion-engine sports car before its electric transition."),
        car(154, "gb", "Lotus", "Elise", "", 1996, "Medium", "A lightweight roadster built around an extruded-aluminum tub."),
        car(155, "gb", "Jaguar", "F-Type", "R", 2014, "Medium", "A supercharged V8 sports car in Jaguar's modern lineup."),
        car(156, "gb", "Jaguar", "XJ220", "", 1992, "Hard", "A twin-turbo V6 supercar that briefly held the world speed record."),
        car(157, "gb", "Mini", "John Cooper Works", "", 2021, "Easy", "The performance flagship of the modern Mini range."),
        car(158, "gb", "TVR", "Sagaris", "", 2004, "Hard", "An aggressive, driver-focused British sports car with no airbags."),
        car(159, "gb", "Morgan", "Aero 8", "", 2001, "Hard", "A hand-built British sports car blending classic looks with an aluminum chassis."),
        car(160, "gb", "BAC", "Mono", "", 2011, "Hard", "A single-seat, road-legal track car built in small numbers.")
    )

    private val carsByRegion: Map<String, List<Car>> = cars.groupBy { it.regionId }
    private val carsById: Map<Int, Car> = cars.associateBy { it.id }

    private val objectiveFragments: Map<String, List<String>> = mapOf(
        "cn" to listOf("SHANGHAI INTERNATIONAL CIRCUIT", "GREAT WALL MOUNTAIN PASS", "BEIJING RING ROAD LOOP", "ZHUHAI CIRCUIT ESSES", "GUANGZHOU NIGHT SPRINT"),
        "us" to listOf("WILLOW SPRINGS BIG WILLOW", "ROUTE 66 DESERT STRAIGHT", "DETROIT MOTOR CITY LOOP", "LAGUNA SECA CORKSCREW", "DRAG STRIP QUARTER MILE"),
        "jp" to listOf("TSUKUBA CIRCUIT INFIELD", "MT. HARUNA TOUGE SECTOR", "SUZUKA DEGENA CURVES", "HAKONE SKYLINE PASS", "FUJI SPEEDWAY STRAIGHT", "SHUTO EXPRESSWAY NIGHT LOOP"),
        "de" to listOf("NÜRBURGRING NORDSCHLEIFE", "AUTOBAHN DERESTRICTED ZONE", "HOCKENHEIMRING HAIRPIN", "BLACK FOREST PASS", "STUTTGART PROVING GROUND"),
        "kr" to listOf("INJE SPEEDIUM CHICANE", "NAMSAN TUNNEL SPRINT", "YEONGDONG HIGHWAY RUN", "BUSAN COASTAL CURVE", "SEOUL RING ROAD LOOP"),
        "it" to listOf("MONZA PARABOLICA", "MILLE MIGLIA ROUTE", "AMALFI COAST HAIRPINS", "MUGELLO CIRCUIT ESSES", "MARANELLO TEST TRACK"),
        "fr" to listOf("CIRCUIT DE LA SARTHE", "COL DE TURINI HAIRPINS", "PARIS RING EXPRESSWAY", "PROVENCE MOUNTAIN PASS", "PAUL RICARD STRAIGHT"),
        "gb" to listOf("SILVERSTONE NATIONAL LOOP", "GOODWOOD HILLCLIMB", "NORTH CIRCULAR SPRINT", "WELSH VALLEY PASS", "BRANDS HATCH DIP")
    )

    const val LEVELS_PER_REGION = 20

    /** Level N in a region is that region's Nth car, in the fixed order above — every level maps to one unique car. */
    fun stagesForRegion(regionId: String): List<Stage> {
        val pool = carsByRegion[regionId].orEmpty()
        val fragments = objectiveFragments[regionId].orEmpty().ifEmpty { listOf("TRIVIA CHALLENGE") }

        return pool.mapIndexed { index, car ->
            val level = index + 1
            val fragment = fragments[index % fragments.size]
            Stage(
                id = level,
                levelNumber = level,
                name = "$fragment • SECTOR $level",
                regionId = regionId,
                carId = car.id,
                starsEarned = 0,
                isCompleted = false,
                isUnlocked = level == 1,
                objectiveName = "$fragment • SECTOR $level"
            )
        }
    }

    fun carForLevel(regionId: String, level: Int): Car? =
        carsByRegion[regionId].orEmpty().getOrNull(level - 1)

    fun carById(id: Int): Car? = carsById[id]

    /** Correct answer + 3 wrong answers, preferring cars from the same region; result is pre-shuffled. */
    fun optionsFor(car: Car): List<String> {
        val sameRegionPool = carsByRegion[car.regionId].orEmpty().filter { it.id != car.id }
        val distractors = if (sameRegionPool.size >= 3) {
            sameRegionPool.shuffled().take(3)
        } else {
            val remainder = cars.filter { it.id != car.id && it !in sameRegionPool }.shuffled()
            (sameRegionPool + remainder).take(3)
        }
        return (listOf(car) + distractors).map { it.displayName }.shuffled()
    }
}
