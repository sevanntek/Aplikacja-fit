# Plan treningowy Android

Instalowalna aplikacja Android do śledzenia realizacji planu treningowego od października do grudnia 2026.

## Funkcje
- Październik / Listopad / Grudzień 2026.
- Każdy kafelek ma dzień tygodnia i datę, np. `Pon • 26 Paź`.
- Sobota i niedziela są pogrubione.
- Trening siłowy pokazuje tylko partie ciała; w dni regeneracyjne pokazuje `Wolny`.
- Cardio ma dwie zamienne opcje: bieżnia/spacer albo rower stacjonarny, z czasem.
- Cele dnia: dieta, kalorie, białko, brak słodyczy, woda, kreatyna, suplementy, sen.
- Czerwone / pomarańczowe / zielone oznaczenia postępu.
- Procent realizacji dnia i całego miesiąca.
- Automatyczny zapis lokalny po każdym zaznaczeniu przez `SharedPreferences` — działa bez internetu.

## APK z GitHub Actions
Workflow `.github/workflows/build-apk.yml` buduje `app-debug.apk` po każdym pushu do `main` i można go też uruchomić ręcznie w zakładce Actions.

Po zakończeniu budowania: **Actions → Build Android APK → najnowszy run → Artifacts → plan-treningowy-apk**.
