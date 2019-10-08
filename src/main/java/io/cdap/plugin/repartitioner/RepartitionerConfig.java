/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.plugin.repartitioner;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Macro;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.plugin.PluginConfig;
import io.cdap.cdap.etl.api.FailureCollector;

/**
 * Configuration for the Repartitioner Plugin.
 */
public class RepartitionerConfig extends PluginConfig {
  public static final String PARTITIONS = "partitions";

  @Name(PARTITIONS)
  @Description("Number of partitions the input RDD should be repartitioned into.")
  @Macro
  private Integer partitions;

  @Name("shuffle")
  @Description("Specifies whether the records have to be shuffled.")
  @Macro
  private Boolean shuffle;

  public RepartitionerConfig(Integer partitions, Boolean shuffle) {
    this.partitions = partitions;
    this.shuffle = shuffle;
  }

  private RepartitionerConfig(Builder builder) {
    partitions = builder.partitions;
    shuffle = builder.shuffle;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(RepartitionerConfig copy) {
    return builder()
      .setPartitions(copy.partitions)
      .setShuffle(copy.shuffle);
  }

  public int getPartitions() {
    return partitions;
  }

  public boolean getShuffle() {
    return shuffle;
  }

  public void validate(FailureCollector failureCollector) {
    if (!containsMacro(PARTITIONS) && partitions <= 0) {
      failureCollector.addFailure("Number of partitions should be greater than zero.", null)
        .withConfigProperty(PARTITIONS);
    }
  }

  /**
   * Builder for RepartitionerConfig
   */
  public static final class Builder {
    private Integer partitions;
    private Boolean shuffle;

    private Builder() {
    }

    public static Builder aRepartitionerConfig() {
      return new Builder();
    }

    public Builder setPartitions(Integer partitions) {
      this.partitions = partitions;
      return this;
    }

    public Builder setShuffle(Boolean shuffle) {
      this.shuffle = shuffle;
      return this;
    }

    public RepartitionerConfig build() {
      return new RepartitionerConfig(this);
    }
  }
}
