/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package hr.miz.evidencijakontakata.Utilities.ExposureNotifications;

import com.google.android.gms.nearby.exposurenotification.ExposureConfiguration;

public class ExposureConfigurations {

    public static final long EXPOSURE_VALIDITY_LIFETIME = 15 * 24 * 60 * 60 * 1000;

    public static ExposureConfiguration get() {
        return new ExposureConfiguration.ExposureConfigurationBuilder()
                .setDurationAtAttenuationThresholds(50, 70)
                .setAttenuationWeight(50)
                .setMinimumRiskScore(1)
                .setTransmissionRiskWeight(50)
                .setDaysSinceLastExposureWeight(50)
                .setDaysSinceLastExposureScores(1, 2, 2, 4, 6, 8, 8, 8)
                .setAttenuationScores(0, 0, 1, 6, 6, 7, 8, 8)
                .setTransmissionRiskScores(8, 8, 8, 8, 8, 8, 8, 8)
                .setDurationScores(1, 1, 5, 8, 8, 8, 8, 8)
                .build();
    }
}
