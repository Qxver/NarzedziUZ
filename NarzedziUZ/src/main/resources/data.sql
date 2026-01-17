-- 1. Wstawianie kategorii (bez podawania ID, baza sama nada)
INSERT INTO category (name) VALUES
                                ('Elektronarzędzia'),
                                ('Narzędzia ręczne'),
                                ('Ogród'),
                                ('Artykuły malarskie'),
                                ('Miernictwo'),
                                ('Artykuły BHP'),
                                ('Osprzęt i akcesoria');

-- 2. Wstawianie produktów z dynamicznym pobieraniem ID kategorii
-- Dzięki temu nie ma znaczenia, czy ID to 1, czy 50.

-- Część 1: Elektronarzędzia
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Wiertarka udarowa Neo Tools 500W', 'Solidna wiertarka udarowa idealna do prac domowych i warsztatowych. Wariant: 500W.', 191.78, 15, c.category_id, 'Neo Tools', 'products_images/wiertarka_udarowa_5.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Wiertarka udarowa Śnieżka 750W', 'Wytrzymały sprzęt do zadań specjalnych. Wariant: 750W.', 554.15, 0, c.category_id, 'Śnieżka', 'products_images/wiertarka_udarowa_3.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Wiertarka udarowa DeWalt 1000W', 'Profesjonalna wiertarka o dużej mocy. Wariant: 1000W.', 726.78, 28, c.category_id, 'DeWalt', 'products_images/wiertarka_udarowa_2.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Wkrętarka akumulatorowa Śnieżka 12V', 'Lekka i poręczna wkrętarka z akumulatorem litowo-jonowym. Wariant: 12V.', 191.72, 24, c.category_id, 'Śnieżka', 'products_images/wkretarka_akumulatorowa_3.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Wkrętarka akumulatorowa Makita 18V', 'Niezawodna wkrętarka do codziennego użytku. Wariant: 18V.', 445.07, 44, c.category_id, 'Makita', 'products_images/wkretarka_akumulatorowa_3.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Wkrętarka akumulatorowa Bosch 20V', 'Wkrętarka z systemem szybkiego ładowania. Wariant: 20V.', 277.60, 27, c.category_id, 'Bosch', 'products_images/wkretarka_akumulatorowa_1.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia';

-- Część 2: Szlifierki i inne Elektronarzędzia
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Szlifierka kątowa Neo Tools 125mm', 'Wytrzymała szlifierka kątowa z systemem antyrestart. Moc 800W.', 350.36, 12, c.category_id, 'Neo Tools', 'products_images/szlifierka_katowa_3.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Szlifierka kątowa Neo Tools 230mm', 'Duża szlifierka do cięcia betonu i stali. Moc 2000W.', 263.04, 8, c.category_id, 'Neo Tools', 'products_images/szlifierka_katowa_4.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Pilarka tarczowa DeWalt 1200W', 'Precyzyjna pilarka do cięcia drewna i materiałów drewnopochodnych.', 366.64, 35, c.category_id, 'DeWalt', 'products_images/pilarka_tarczowa_2.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Pilarka tarczowa Bosch 1400W', 'Pilarka z prowadnicą laserową. Wariant: 1400W.', 368.34, 40, c.category_id, 'Bosch', 'products_images/pilarka_tarczowa_4.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Wyrzynarka Makita 650W', 'Wyrzynarka z regulacją obrotów i podcinaniem.', 144.63, 47, c.category_id, 'Makita', 'products_images/wyrzynarka_2.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Wyrzynarka Stanley 800W', 'Wyrzynarka do precyzyjnego cięcia krzywoliniowego.', 447.23, 18, c.category_id, 'Stanley', 'products_images/wyrzynarka_5.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia';

-- Część 3: Narzędzia ręczne
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Młotek ślusarski Makita 300g', 'Klasyczny młotek ślusarski z trzonkiem z drewna jesionowego. 300g.', 69.35, 46, c.category_id, 'Makita', 'products_images/mlotek_slusarski_2.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Młotek ślusarski Neo Tools 500g', 'Młotek kuty matrycowo, hartowany indukcyjnie. 500g.', 51.61, 6, c.category_id, 'Neo Tools', 'products_images/mlotek_slusarski_3.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Młotek ślusarski Fiskars 1000g', 'Ciężki młotek do prac warsztatowych. 1000g.', 29.84, 35, c.category_id, 'Fiskars', 'products_images/mlotek_slusarski_4.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Młotek ślusarski Bosch 2kg', 'Młotek z ergonomicznym uchwytem antypoślizgowym. 2kg.', 48.04, 41, c.category_id, 'Bosch', 'products_images/mlotek_slusarski_1.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Młotek gumowy Stanley czarny', 'Młotek gumowy do prac blacharskich i kafelkowania.', 19.85, 35, c.category_id, 'Stanley', 'products_images/mlotek_gumowy_4.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Młotek ciesielski Neo Tools 600g', 'Młotek ciesielski z magnesem do przytrzymywania gwoździ.', 65.06, 30, c.category_id, 'Neo Tools', 'products_images/mlotek_ciesielski_2.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne';

-- Część 4: Klucze i Śrubokręty (Narzędzia ręczne)
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Zestaw kluczy płasko-oczkowych Neo 6-22mm', 'Zestaw 12 kluczy ze stali CrV w praktycznym etui.', 197.32, 7, c.category_id, 'Neo Tools', 'products_images/zestaw_kluczy_4.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Zestaw kluczy płasko-oczkowych Yato 8-32mm', 'Duży zestaw kluczy do warsztatu samochodowego.', 337.47, 30, c.category_id, 'Yato', 'products_images/zestaw_kluczy_2.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Śrubokręt płaski Yato 3x75mm', 'Wkrętak płaski z magnetyczną końcówką.', 12.62, 15, c.category_id, 'Yato', 'products_images/srubokret_plaski_5.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Śrubokręt płaski Dedra 5x100mm', 'Wkrętak precyzyjny ze stali S2.', 19.24, 25, c.category_id, 'Dedra', 'products_images/srubokret_plaski_1.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Śrubokręt krzyżakowy Fiskars PH1', 'Wkrętak krzyżakowy Philips z antypoślizgową rączką.', 15.48, 21, c.category_id, 'Fiskars', 'products_images/srubokret_krzyzakowy_2.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Śrubokręt krzyżakowy Neo Tools PH2', 'Uniwersalny wkrętak krzyżakowy do domu.', 21.46, 15, c.category_id, 'Neo Tools', 'products_images/srubokret_krzyzakowy_4.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Kombinerki uniwersalne Dedra 160mm', 'Szczypce uniwersalne wykonane z utwardzanej stali.', 36.17, 10, c.category_id, 'Dedra', 'products_images/kombinerki_1.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Kombinerki uniwersalne Stanley 180mm', 'Profesjonalne kombinerki z obcinakiem bocznym.', 48.57, 16, c.category_id, 'Stanley', 'products_images/kombinerki_3.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne';


-- Część 5: Ogród
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Szpadel prosty Dedra metalowy', 'Wytrzymały szpadel do kopania w twardej glebie.', 89.49, 31, c.category_id, 'Dedra', 'products_images/szpadel_prosty_3.jpg'
FROM category c WHERE c.name = 'Ogród'
UNION ALL
SELECT 'Szpadel prosty Yato drewniany', 'Szpadel hartowany z ergonomicznym trzonkiem.', 62.77, 0, c.category_id, 'Yato', 'products_images/szpadel_prosty_3.jpg'
FROM category c WHERE c.name = 'Ogród'
UNION ALL
SELECT 'Grabie ogrodowe Stanley 12-zębne', 'Grabie metalowe idealne do równania ziemi.', 68.19, 4, c.category_id, 'Stanley', 'products_images/grabie_ogrodowe_5.jpg'
FROM category c WHERE c.name = 'Ogród'
UNION ALL
SELECT 'Sekator ręczny Dedra kowadełkowy', 'Ostry sekator do przycinania suchych gałęzi.', 40.81, 13, c.category_id, 'Dedra', 'products_images/sekator_reczny_4.jpg'
FROM category c WHERE c.name = 'Ogród'
UNION ALL
SELECT 'Sekator ręczny Makita nożycowy', 'Sekator do precyzyjnego cięcia kwiatów i krzewów.', 55.03, 5, c.category_id, 'Makita', 'products_images/sekator_reczny_5.jpg'
FROM category c WHERE c.name = 'Ogród'
UNION ALL
SELECT 'Łopata piaskowa DeWalt szeroka', 'Lekka łopata aluminiowa do materiałów sypkich.', 79.87, 6, c.category_id, 'DeWalt', 'products_images/lopata_piaskowa_2.jpg'
FROM category c WHERE c.name = 'Ogród';

-- Część 6: Artykuły malarskie
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Pędzel angielski Makita 30mm', 'Pędzel płaski do farb olejnych i lakierów.', 8.85, 28, c.category_id, 'Makita', 'products_images/pedzel_angielski_4.jpg'
FROM category c WHERE c.name = 'Artykuły malarskie'
UNION ALL
SELECT 'Pędzel angielski Śnieżka 50mm', 'Uniwersalny pędzel do farb emulsyjnych.', 11.02, 27, c.category_id, 'Śnieżka', 'products_images/pedzel_angielski_3.jpg'
FROM category c WHERE c.name = 'Artykuły malarskie'
UNION ALL
SELECT 'Wałek malarski Śnieżka 18cm', 'Wałek z mikrofibry do gładkich powierzchni.', 22.87, 22, c.category_id, 'Śnieżka', 'products_images/walek_malarski_4.jpg'
FROM category c WHERE c.name = 'Artykuły malarskie'
UNION ALL
SELECT 'Wałek malarski Stanley 25cm', 'Szeroki wałek malarski niekapiący.', 32.93, 32, c.category_id, 'Stanley', 'products_images/walek_malarski_2.jpg'
FROM category c WHERE c.name = 'Artykuły malarskie'
UNION ALL
SELECT 'Kuweta malarska Stanley mała', 'Kuweta z tworzywa sztucznego odporna na rozpuszczalniki.', 16.60, 34, c.category_id, 'Stanley', 'products_images/kuweta_malarska_2.jpg'
FROM category c WHERE c.name = 'Artykuły malarskie';

-- Część 7: Miernictwo
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Miara zwijana Yato 3m', 'Miara stalowa w gumowanej obudowie z blokadą.', 17.27, 28, c.category_id, 'Yato', 'products_images/miara_zwijana_5.jpg'
FROM category c WHERE c.name = 'Miernictwo'
UNION ALL
SELECT 'Miara zwijana Śnieżka 5m', 'Miara z magnesem na końcówce taśmy.', 28.31, 43, c.category_id, 'Śnieżka', 'products_images/miara_zwijana_5.jpg'
FROM category c WHERE c.name = 'Miernictwo'
UNION ALL
SELECT 'Miara zwijana Neo Tools 8m', 'Profesjonalna miara budowlana, szeroka taśma.', 44.28, 34, c.category_id, 'Neo Tools', 'products_images/miara_zwijana_5.jpg'
FROM category c WHERE c.name = 'Miernictwo'
UNION ALL
SELECT 'Poziomica aluminiowa Bosch 40cm', 'Poziomica z libellami odpornymi na wstrząsy.', 83.32, 30, c.category_id, 'Bosch', 'products_images/poziomica_2.jpg'
FROM category c WHERE c.name = 'Miernictwo'
UNION ALL
SELECT 'Poziomica aluminiowa Bosch 60cm', 'Poziomica anodowana, wysoka precyzja pomiaru.', 99.72, 19, c.category_id, 'Bosch', 'products_images/poziomica_3.jpg'
FROM category c WHERE c.name = 'Miernictwo'
UNION ALL
SELECT 'Kątownik stolarski Neo Tools 250mm', 'Kątownik stalowy do wyznaczania kąta prostego.', 39.44, 19, c.category_id, 'Neo Tools', 'products_images/katownik_1.jpg'
FROM category c WHERE c.name = 'Miernictwo'
UNION ALL
SELECT 'Kątownik stolarski Dedra 300mm', 'Kątownik z podziałką milimetrową.', 34.22, 7, c.category_id, 'Dedra', 'products_images/katownik_1.jpg'
FROM category c WHERE c.name = 'Miernictwo';

-- Część 8: Artykuły BHP
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Rękawice robocze Śnieżka L', 'Rękawice ochronne zapewniające pewny chwyt.', 9.25, 27, c.category_id, 'Śnieżka', 'products_images/rekawice_robocze_4.jpg'
FROM category c WHERE c.name = 'Artykuły BHP'
UNION ALL
SELECT 'Rękawice robocze Bosch XL', 'Rękawice wzmacniane skórą bydlęcą.', 25.11, 12, c.category_id, 'Bosch', 'products_images/rekawice_robocze_5.jpg'
FROM category c WHERE c.name = 'Artykuły BHP'
UNION ALL
SELECT 'Rękawice robocze powlekane nitrylem', 'Rękawice odporne na oleje i smary.', 15.80, 46, c.category_id, 'Śnieżka', 'products_images/rekawice_robocze_2.jpg'
FROM category c WHERE c.name = 'Artykuły BHP'
UNION ALL
SELECT 'Okulary ochronne DeWalt', 'Lekkie okulary chroniące przed odpryskami, przezroczyste.', 34.07, 28, c.category_id, 'DeWalt', 'products_images/okulary_ochronne_1.jpg'
FROM category c WHERE c.name = 'Artykuły BHP'
UNION ALL
SELECT 'Okulary ochronne Yato przyciemniane', 'Okulary przeciwsłoneczne z filtrem UV do prac na zewnątrz.', 23.14, 15, c.category_id, 'Yato', 'products_images/okulary_ochronne_3.jpg'
FROM category c WHERE c.name = 'Artykuły BHP'
UNION ALL
SELECT 'Kask budowlany Żółty', 'Kask ochronny z regulacją obwodu, atestowany.', 49.85, 19, c.category_id, 'Śnieżka', 'products_images/kask_budowlany_5.jpg'
FROM category c WHERE c.name = 'Artykuły BHP'
UNION ALL
SELECT 'Kask budowlany Biały', 'Kask ochronny dla kadry kierowniczej.', 52.30, 34, c.category_id, 'Śnieżka', 'products_images/kask_budowlany_1.jpg'
FROM category c WHERE c.name = 'Artykuły BHP';

-- Część 9: Osprzęt i akcesoria
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Tarcza do cięcia metalu 125x1.0mm', 'Wydajna tarcza korundowa do cięcia stali.', 5.36, 150, c.category_id, 'Fiskars', 'products_images/tarcza_metal_1.jpg'
FROM category c WHERE c.name = 'Osprzęt i akcesoria'
UNION ALL
SELECT 'Tarcza do cięcia metalu 230x2.0mm', 'Tarcza do cięcia grubych profili stalowych.', 9.08, 119, c.category_id, 'Makita', 'products_images/tarcza_metal_5.jpg'
FROM category c WHERE c.name = 'Osprzęt i akcesoria'
UNION ALL
SELECT 'Zestaw wierteł do metalu HSS 1-10mm', 'Zestaw wierteł ze stali szybkotnącej w pudełku.', 48.74, 33, c.category_id, 'Yato', 'products_images/wiertla_metal_1.jpg'
FROM category c WHERE c.name = 'Osprzęt i akcesoria'
UNION ALL
SELECT 'Zestaw wierteł do betonu SDS+ 5-12mm', 'Wiertła udarowe z końcówką z węglika spiekanego.', 65.83, 34, c.category_id, 'Makita', 'products_images/wiertla_beton_1.jpg'
FROM category c WHERE c.name = 'Osprzęt i akcesoria';

-- Część 10: Produkty PRO (Różne kategorie - trzeba dopasować)
-- Wiertarka/Szlifierka -> Elektronarzędzia
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Szlifierka kątowa Neo Tools PRO', 'Wersja Heavy Duty do pracy ciągłej.', 422.89, 85, c.category_id, 'Neo Tools', 'products_images/szlifierka_generic.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Wiertarka udarowa Neo Tools PRO', 'Wiertarka z metalowym uchwytem wiertarskim.', 255.42, 54, c.category_id, 'Neo Tools', 'products_images/wiertarka_generic.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Wiertarka udarowa Makita PRO', 'Wiertarka o podwyższonej odporności na pył.', 591.67, 7, c.category_id, 'Makita', 'products_images/wiertarka_generic.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Wyrzynarka Fiskars PRO', 'Wyrzynarka z systemem beznarzędziowej wymiany brzeszczotów.', 273.41, 78, c.category_id, 'Fiskars', 'products_images/wyrzynarka_generic.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Pilarka tarczowa Yato PRO', 'Pilarka z tarczą widiową w zestawie.', 324.94, 51, c.category_id, 'Yato', 'products_images/pilarka_generic.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia'
UNION ALL
SELECT 'Szlifierka kątowa Stanley PRO', 'Szlifierka z regulacją obrotów 3000-11000.', 224.92, 15, c.category_id, 'Stanley', 'products_images/szlifierka_generic.jpg'
FROM category c WHERE c.name = 'Elektronarzędzia';

-- Młotek/Śrubokręt -> Narzędzia ręczne
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Młotek gumowy Fiskars czarny PRO', 'Młotek bezodrzutowy, profesjonalny.', 41.33, 60, c.category_id, 'Fiskars', 'products_images/mlotek_generic.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Młotek gumowy Fiskars biały PRO', 'Młotek do prac montażowych, nie brudzi powierzchni.', 34.56, 69, c.category_id, 'Fiskars', 'products_images/mlotek_generic.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Zestaw kluczy Stanley PRO', 'Klucze polerowane na wysoki połysk.', 296.69, 70, c.category_id, 'Stanley', 'products_images/zestaw_generic.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Śrubokręt płaski Neo Tools PRO', 'Wkrętak do pobijania ze stalowym kapslem.', 22.12, 85, c.category_id, 'Neo Tools', 'products_images/srubokret_generic.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne'
UNION ALL
SELECT 'Śrubokręt krzyżakowy Bosch PH3 PRO', 'Wkrętak izolowany 1000V dla elektryków.', 40.99, 65, c.category_id, 'Bosch', 'products_images/srubokret_generic.jpg'
FROM category c WHERE c.name = 'Narzędzia ręczne';

-- Malarskie
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Wałek malarski Dedra 18cm PRO', 'Profesjonalny wałek malarski, nie zostawia smug.', 36.35, 21, c.category_id, 'Dedra', 'products_images/walek_generic.jpg'
FROM category c WHERE c.name = 'Artykuły malarskie'
UNION ALL
SELECT 'Pędzel angielski DeWalt 50mm PRO', 'Profesjonalny pędzel z naturalnego włosia.', 18.82, 14, c.category_id, 'DeWalt', 'products_images/pedzel_generic.jpg'
FROM category c WHERE c.name = 'Artykuły malarskie'
UNION ALL
SELECT 'Kuweta malarska Fiskars mała PRO', 'Kuweta z ociekaczem, wzmocniona.', 17.83, 49, c.category_id, 'Fiskars', 'products_images/kuweta_generic.jpg'
FROM category c WHERE c.name = 'Artykuły malarskie';

-- Miernictwo
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Kątownik stolarski Neo Tools 300mm PRO', 'Kątownik precyzyjny, klasa dokładności II.', 41.09, 19, c.category_id, 'Neo Tools', 'products_images/katownik_generic.jpg'
FROM category c WHERE c.name = 'Miernictwo'
UNION ALL
SELECT 'Miara zwijana Fiskars 8m PRO', 'Miara z taśmą nylonową, odporna na ścieranie.', 61.33, 55, c.category_id, 'Fiskars', 'products_images/miara_generic.jpg'
FROM category c WHERE c.name = 'Miernictwo'
UNION ALL
SELECT 'Kątownik stolarski Fiskars 300mm PRO', 'Kątownik aluminiowy, lekki.', 34.33, 23, c.category_id, 'Fiskars', 'products_images/katownik_generic.jpg'
FROM category c WHERE c.name = 'Miernictwo'
UNION ALL
SELECT 'Miara zwijana Śnieżka 3m PRO', 'Kompaktowa miara kieszonkowa.', 16.61, 65, c.category_id, 'Śnieżka', 'products_images/miara_generic.jpg'
FROM category c WHERE c.name = 'Miernictwo';

-- Ogród
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Łopata piaskowa Dedra PRO', 'Łopata hartowana, wyjątkowo lekka.', 41.41, 21, c.category_id, 'Dedra', 'products_images/lopata_generic.jpg'
FROM category c WHERE c.name = 'Ogród'
UNION ALL
SELECT 'Szpadel prosty Stanley PRO', 'Szpadel z systemem amortyzacji drgań.', 93.61, 80, c.category_id, 'Stanley', 'products_images/szpadel_generic.jpg'
FROM category c WHERE c.name = 'Ogród'
UNION ALL
SELECT 'Grabie ogrodowe Makita PRO', 'Grabie wachlarzowe do liści z regulacją.', 49.36, 40, c.category_id, 'Makita', 'products_images/grabie_generic.jpg'
FROM category c WHERE c.name = 'Ogród'
UNION ALL
SELECT 'Grabie ogrodowe Dedra PRO', 'Grabie do siana, tworzywo sztuczne.', 27.23, 86, c.category_id, 'Dedra', 'products_images/grabie_generic.jpg'
FROM category c WHERE c.name = 'Ogród'
UNION ALL
SELECT 'Szpadel prosty Śnieżka PRO', 'Szpadel drenarski, wąski.', 73.76, 100, c.category_id, 'Śnieżka', 'products_images/szpadel_generic.jpg'
FROM category c WHERE c.name = 'Ogród';

-- BHP
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Kask budowlany DeWalt PRO', 'Kask z wentylacją i uchwytem na latarkę.', 60.18, 97, c.category_id, 'DeWalt', 'products_images/kask_generic.jpg'
FROM category c WHERE c.name = 'Artykuły BHP'
UNION ALL
SELECT 'Okulary ochronne Bosch PRO', 'Okulary gogle, pełna ochrona oczu.', 51.64, 95, c.category_id, 'Bosch', 'products_images/okulary_generic.jpg'
FROM category c WHERE c.name = 'Artykuły BHP';

-- Osprzęt
INSERT INTO product (name, description, price, quantity, category_id, manufacturer, photo)
SELECT 'Zestaw wierteł do betonu Yato PRO', 'Zestaw wierteł czteroostrzowych SDS Max.', 102.00, 12, c.category_id, 'Yato', 'products_images/zestaw_generic.jpg'
FROM category c WHERE c.name = 'Osprzęt i akcesoria'
UNION ALL
SELECT 'Zestaw wierteł do betonu Stanley PRO', 'Wiertła do betonu zbrojonego.', 150.96, 100, c.category_id, 'Stanley', 'products_images/zestaw_generic.jpg'
FROM category c WHERE c.name = 'Osprzęt i akcesoria'
UNION ALL
SELECT 'Zestaw wierteł do metalu Makita PRO', 'Wiertła kobaltowe do stali nierdzewnej.', 96.81, 86, c.category_id, 'Makita', 'products_images/zestaw_generic.jpg'
FROM category c WHERE c.name = 'Osprzęt i akcesoria'
UNION ALL
SELECT 'Zestaw wierteł do metalu DeWalt PRO', 'Zestaw wierteł tytanowych.', 171.97, 13, c.category_id, 'DeWalt', 'products_images/zestaw_generic.jpg'
FROM category c WHERE c.name = 'Osprzęt i akcesoria';
