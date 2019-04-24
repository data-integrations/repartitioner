<a href="https://cdap-users.herokuapp.com/"><img alt="Join CDAP community" src="https://cdap-users.herokuapp.com/badge.svg?t=repartitioner"/></a> [![Build Status](https://travis-ci.org/hydrator/repartitioner.svg?branch=develop)](https://travis-ci.org/hydrator/repartitioner) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![cm-available](https://cdap-users.herokuapp.com/assets/cm-available.svg)](https://docs.cask.co/cdap/current/en/integrations/cask-market.html) <img  alt="CDAP Spark Compute" src="https://cdap-users.herokuapp.com/assets/cdap-sparkcompute.svg"/>

# Repartitioner

Repartitioner partitions a RDD and define how it gets spread out over a cluster. 

## Plugin Configuration

| Configuration | Required | Default | Description |
| :------------ | :------: | :----- | :---------- |
| **Partitions** | **N** | 1 | Specifies the number of partitions or level of parallelism to be set for the RDD emitted out of this plugin. |
| **Shuffle Data** | **N** | False | Specifies whether the data need to be shuffled during repartitioning of RDD. |

## Usage Notes

By default the behavior of repartitioning is similar to ```coalesce``` in which case, you decrease number of partition of RDD without shuffling data over the network. You can use this plugin to reduce or increase partitions of an RDD. If you are decreasing partition it's a good practise not to shuffle the data -- in doing so, it will keep the data on the number of nodes and pull in the remaining data from other nodes. 

If you are using this plugin to increase the partitions it's a good practise to shuffle the data, there is a cost associated with shuffling, but evening out data over the partitions helps improve the performance. 

## Clone this repo
Clone the this repo to your local environment

```
  git clone https://github.com/hydrator/repartitioner.git repartitioner
```

# Build

## Clone this repo
Clone the this repo to your local environment

```
  git clone https://github.com/hydrator/repartitioner.git repartitioner
```

## Build

To build your plugins:

    mvn clean package -DskipTests

The build will create a .jar and .json file under the ``target`` directory.
These files can be used to deploy your plugins.

## Deployment
You can deploy your plugins using the CDAP CLI:

    > load artifact <target/repartitioner-<version>.jar> config-file <target/repartitioner-<version>.json>

For example, if your artifact is named 'repartitioner-<version>:

    > load artifact target/repartitioner-<version>.jar config-file target/repartitioner-<version>.json

# Mailing Lists

CDAP User Group and Development Discussions:

- `cdap-user@googlegroups.com <https://groups.google.com/d/forum/cdap-user>`__

The *cdap-user* mailing list is primarily for users using the product to develop
applications or building plugins for appplications. You can expect questions from 
users, release announcements, and any other discussions that we think will be helpful 
to the users.


# License and Trademarks

Copyright © 2016-2019 Cask Data, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the 
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
either express or implied. See the License for the specific language governing permissions 
and limitations under the License.

Cask is a trademark of Cask Data, Inc. All rights reserved.

Apache, Apache HBase, and HBase are trademarks of The Apache Software Foundation. Used with
permission. No endorsement by The Apache Software Foundation is implied by the use of these marks.

