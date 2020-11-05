# Repartitioner

This plugin repartitions incoming data. This changes the level of parallelism of the pipeline.

Increasing partitions is usually done to increase parallelism when trying to speed up a pipeline.
For example, the database source is often configured to use a relatively small number of partitions
(tens, but not hundreds or thousands). This is to prevent a flood of concurrent connections to the database,
which would overwhelm the database. However, once all the data is read, it can sometimes make sense to
repartition to a much higher number to allow the rest of the pipeline to run with more parallelism.

Decreasing partitions is done when you want to limit the amount of parallelism.
For example, if you want all of the output written to a single file, you can decrease the partitions to 1
before writing to a file sink. As another example, you may want to decrease the partitions when writing to a
database sink to limit the amount of concurrent connections open to the database. As another example,
you may want to decrease the partitions before using a transform that makes API calls to an external service,
in order to avoid hitting rate limits or causing high load on that external service.

## Plugin Properties

**Partitions:** Number of partitions to generate

**Shuffle Data:** Whether to shuffle data when repartitioning.
When set to true, incoming data will be shuffled across the cluster and the rest of the
pipeline (up to another shuffle plugin) will be executed using that many partitions.
This requires shuffling all the incoming data across the cluster,
which can be a lot of I/O if the incoming data is large.
When set to false, this hints to the engine that partitions should be combined locally when possible.
This may result in the engine changing the parallelism for earlier parts of the pipeline.

For example, suppose a source that reads data into 50 partitions is connected to a repartitioner
that decreases the partitions to 10.

If data is shuffled, 50 parallel tasks will be used to read the 50 source partitions.
Afterward, data will be shuffled across the cluster down into 10 new partitions. 
The rest of the pipeline will be handled by 10 parallel tasks, each processing a single partition.

If data is not shuffled, instead of reading the 50 source partitions with 50 parallel tasks,
the engine will instead use 10 parallel tasks, each one reading 5 partitions. Each task will
then locally coalesce their 5 partitions down into a single one.

Similarly, suppose a source that reads data into 10 partitions is connected to a repartitioner
that increases the partitions to 50. 

If data is shuffled, 10 parallel tasks will be used to read the 10 source partitions.
Afterward, data will be shuffled across the cluster up into 50 new partitions.
The rest of the pipeline will be handled by 50 parallel tasks, each processing a single partition.

If data is not shuffled, 10 parallel tasks will be used to read the 10 source partitions.
Those same 10 tasks will be split each of their partitions into 5 new partitions to
generate a total of 50 partitions.

In summary, shuffling data will always change the level of parallelism after the plugin.
If you do not shuffle data and are increasing the number of partitions, you do not actually
end up changing the level of parallelism.
If you do not shuffle data and are decreasing the number of partitions, you may end up
reducing the level of parallelism before the plugin as well.
