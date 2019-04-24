/*
 * Copyright © 2017-2019 Cask Data, Inc.
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
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.plugin.PluginConfig;
import io.cdap.cdap.etl.api.PipelineConfigurer;
import io.cdap.cdap.etl.api.batch.SparkCompute;
import io.cdap.cdap.etl.api.batch.SparkExecutionPluginContext;
import org.apache.spark.api.java.JavaRDD;

/**
 * Repartitioner Spark Compute Plugin.
 */
@Plugin(type = SparkCompute.PLUGIN_TYPE)
@Name(Repartitioner.PLUGIN_NAME)
@Description("Repartitions an RDD.")
public class Repartitioner extends SparkCompute<StructuredRecord, StructuredRecord> {

  public static final String PLUGIN_NAME = "Repartitioner";
  private Config config;

  public Repartitioner(Config config) {
    this.config = config;
  }

  @Override
  public void configurePipeline(PipelineConfigurer pipelineConfigurer) throws IllegalArgumentException {
    super.configurePipeline(pipelineConfigurer);

    if (config.getPartitions(1) > 0) {
      throw new IllegalArgumentException("Number of partitions should be greater than zero.");
    }
  }

  @Override
  public void initialize(SparkExecutionPluginContext context) throws Exception {
    super.initialize(context);
  }

  @Override
  public JavaRDD<StructuredRecord> transform(SparkExecutionPluginContext context,
                                             JavaRDD<StructuredRecord> input) {
    return input.coalesce(config.getPartitions(input.getNumPartitions()), config.getShuffle());
  }

  /**
   * Configuration for the Repartitioner Plugin.
   */
  @SuppressWarnings("unused")
  public static class Config extends PluginConfig {
    @Name("partitions")
    @Description("Number of partitions the input RDD should be repartitioned into.")
    @Macro
    private String partitions;

    @Name("shuffle")
    @Description("Specifies whether the records have to be shuffled.")
    @Macro
    private String shuffle;

    public int getPartitions(int defaultValue) {
      try {
        return Integer.parseInt(partitions);
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    }

    public boolean getShuffle() {
      return Boolean.parseBoolean(shuffle);
    }
  }
}
