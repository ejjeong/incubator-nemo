/*
 * Copyright (C) 2017 Seoul National University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.snu.onyx.compiler;

import edu.snu.onyx.client.JobConf;
import edu.snu.onyx.client.JobLauncher;
import edu.snu.onyx.compiler.frontend.Frontend;
import edu.snu.onyx.compiler.frontend.beam.BeamFrontend;
import edu.snu.onyx.compiler.ir.*;
import edu.snu.onyx.compiler.optimizer.policy.*;
import edu.snu.onyx.examples.beam.*;
import edu.snu.onyx.common.dag.DAG;
import org.apache.reef.tang.Configuration;
import org.apache.reef.tang.Injector;
import org.apache.reef.tang.Tang;

/**
 * Utility methods for tests.
 */
public final class CompilerTestUtil {
  public static final String rootDir = System.getProperty("user.dir");
  public static final String padoPolicy = PadoPolicy.class.getCanonicalName();
  public static final String sailfishDisaggPolicy = SailfishDisaggPolicy.class.getCanonicalName();
  public static final String defaultPolicy = DefaultPolicy.class.getCanonicalName();
  public static final String dataSkewPolicy = DataSkewPolicy.class.getCanonicalName();

  public static DAG<IRVertex, IREdge> compileMRDAG() throws Exception {
    final Frontend beamFrontend = new BeamFrontend();
    final ArgBuilder mrArgBuilder = MapReduceITCase.builder;
    final Configuration configuration = JobLauncher.getJobConf(mrArgBuilder.build());
    final Injector injector = Tang.Factory.getTang().newInjector(configuration);
    final String className = injector.getNamedInstance(JobConf.UserMainClass.class);
    final String[] arguments = injector.getNamedInstance(JobConf.UserMainArguments.class).split(" ");

    return beamFrontend.compile(className, arguments);
  }

  public static DAG<IRVertex, IREdge> compileALSDAG() throws Exception {
    final Frontend beamFrontend = new BeamFrontend();
    final ArgBuilder alsArgBuilder = AlternatingLeastSquareITCase.builder;
    final Configuration configuration = JobLauncher.getJobConf(alsArgBuilder.build());
    final Injector injector = Tang.Factory.getTang().newInjector(configuration);
    final String className = injector.getNamedInstance(JobConf.UserMainClass.class);
    final String[] arguments = injector.getNamedInstance(JobConf.UserMainArguments.class).split(" ");

    return beamFrontend.compile(className, arguments);
  }

  public static DAG<IRVertex, IREdge> compileALSInefficientDAG() throws Exception {
    final String alsInefficient = "edu.snu.onyx.examples.beam.AlternatingLeastSquareInefficient";
    final String input = rootDir + "/src/main/resources/sample_input_als";
    final String numFeatures = "10";
    final String numIteration = "3";
    final String dagDirectory = "./dag";

    final Frontend beamFrontend = new BeamFrontend();
    final ArgBuilder alsArgBuilder = new ArgBuilder()
        .addJobId(AlternatingLeastSquareInefficient.class.getSimpleName())
        .addUserMain(alsInefficient)
        .addUserArgs(input, numFeatures, numIteration)
        .addDAGDirectory(dagDirectory);
    final Configuration configuration = JobLauncher.getJobConf(alsArgBuilder.build());
    final Injector injector = Tang.Factory.getTang().newInjector(configuration);
    final String className = injector.getNamedInstance(JobConf.UserMainClass.class);
    final String[] arguments = injector.getNamedInstance(JobConf.UserMainArguments.class).split(" ");

    return beamFrontend.compile(className, arguments);
  }

  public static DAG<IRVertex, IREdge> compileMLRDAG() throws Exception {
    final Frontend beamFrontend = new BeamFrontend();
    final ArgBuilder alsArgBuilder = MultinomialLogisticRegressionITCase.builder;
    final Configuration configuration = JobLauncher.getJobConf(alsArgBuilder.build());
    final Injector injector = Tang.Factory.getTang().newInjector(configuration);
    final String className = injector.getNamedInstance(JobConf.UserMainClass.class);
    final String[] arguments = injector.getNamedInstance(JobConf.UserMainArguments.class).split(" ");

    return beamFrontend.compile(className, arguments);
  }
}
