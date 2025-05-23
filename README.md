# 📱 Admob Ads Implementation

This Android project provides a modular implementation of all major AdMob ad formats, controlled via Firebase Remote Config. It supports:

- ✅ Adaptive & Collapsible Banners
- ✅ Native Ads
- ✅ Interstitial Ads
- ✅ Rewarded Ads
- ✅ Rewarded Interstitial Ads
- ✅ App Open Ads

---

## ✅ Key Functionalities Handled

- Firebase Remote Config for ad control (enable/disable ads remotely)
- Premium user check to disable ads for subscribed users
- Internet connectivity verification before showing ads
- Activity lifecycle safety (prevent loading ads in destroyed/finishing activities)
- Ad ID validation (ensure non-empty ad unit IDs)
- Collapsible banner position handling (top/bottom)
- Auto adaptive banner sizing based on screen dimensions
- Ad listener callbacks (loaded, failed, clicked, impression, closed)
- Placeholder visibility management for smooth UI experience
- Preloading logic for interstitial, rewarded, and rewarded interstitial ads

---

## 🧩 Supported Ad Types

- **Banner Ads**: Adaptive and collapsible formats with smart placement.
- **Native Ads**: Configurable templates with UI component support.
- **Interstitial Ads**: Shown between screen transitions or actions.
- **Rewarded Ads**: Offers user incentives.
- **Rewarded Interstitial Ads**: Combines interstitial and rewarded experiences.
- **App Open Ads**: Displayed during app cold/warm start.

---

## 📄 License

Copyright 2022 OrbitalSonic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

---

## 🙌 Contributions

Contributions and pull requests are welcome. Please ensure to test and document changes.

> **Note:** Always test ads using test IDs during development. Only use real IDs in production.