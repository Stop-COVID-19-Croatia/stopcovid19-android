# Stop COVID-19 Hrvatska

![Stop COVID-19](screen.jpg?raw=true "Stop COVID-19 - Obavijesti o izloženosti")

## Description
Kako bi usporili širenje bolesti COVID-19 među populacijom, Vlada Republike Hrvatske i Ministarstvo zdravstva razvili su aplikaciju koja upozorava korisnike da su bili u kontaktu s osobom kojoj je naknadno potvrđena zaraza bolešću COVID-19. Aplikacija se temelji na servisu kojeg su zajednički razvili Google i Apple te koristi Bluetooth tehnologiju za razmjenu nasumičnih anonimnih ključeva između pametnih uređaja korisnika koji su u epidemiološki relevantnom bliskom kontaktu. Ti kriptirani ključevi mijenjaju se nekoliko puta tijekom svakog sata, čime je dodatno zajamčena zaštita privatnosti korisnika. Ako jedan od korisnika naknadno dobije pozitivan laboratorijski nalaz na COVID-19,  može donijeti odluku da tu informaciju podijeli putem aplikacije korištenjem jednokratnog verifikacijskog kôda. Aplikacija potom utvrđuje da li je korisnik bio u kontaktu sa COVID-19 pozitivnom osobom te, ako je, navodi datum ostvarenog kontakta i preporučuje sljedeće korake. Preuzimanje aplikacije je dragovoljno, a korisnik je može isključiti kad god želi. Instalacija i korištenje aplikacije ne zahtijeva registraciju korisnika niti se pritom traže ili bilježe ikakvi osobni podaci, niti se u bilo kojem trenutku prikupljaju geolokacijski podaci korisnika. 

In order to slow down the spread of COVID-19 among population, the Croatian Government-Ministry of Health has developed a mobile application designed to alert users who have been in prolonged contact with someone who subsequently tested positive for Covid-19. The app is based on a service jointly developed by Google and Apple and uses Bluetooth technology to enable the exchange of random “keys” between smart devices of users who are in close proximity. These randomly generated strings of characters are changed several times a day, which guarantees maximum protection of privacy. If a user tests positive, they can choose to share the keys stored on their smartphone. The application then determines which of the contacts carry risk and generates an exposure notification, indicating the day of the exposure and the recommended procedure to follow. The installation is voluntary and the application can be turned off at any time.  Installation and use do not require registration, the app does not record any personal data or geolocation data of the users.


## Repositories
| Repository  | Description                           |
| --------------|:-------------------------------|
| [stopcovid19-android](../../../stop-covid19-android)  | Android app using the Apple/Google exposure notification API. |
| [stopcovid19-ios](../../../stop-covid19-ios)  | iOS app using the Apple/Google exposure notification API. |
| [stopcovid19-docs](../../../stop-covid19-docs)  | Documentation for the Apple/Google exposure notification API platform. |


## Licensing
Copyright (c) 2020 APIS IT d.o.o.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.

You may obtain a copy of the License at https://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the LICENSE for the specific language governing permissions and limitations under the License.
