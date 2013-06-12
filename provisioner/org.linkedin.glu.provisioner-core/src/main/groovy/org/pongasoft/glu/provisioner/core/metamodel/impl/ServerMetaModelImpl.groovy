/*
 * Copyright (c) 2013 Yan Pujante
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

package org.pongasoft.glu.provisioner.core.metamodel.impl

import org.linkedin.glu.groovy.utils.collections.GluGroovyCollectionUtils
import org.pongasoft.glu.provisioner.core.metamodel.ConfigMetaModel
import org.pongasoft.glu.provisioner.core.metamodel.HostMetaModel
import org.pongasoft.glu.provisioner.core.metamodel.ServerMetaModel

/**
 * @author yan@pongasoft.com  */
public class ServerMetaModelImpl implements ServerMetaModel
{
  public static final String MAIN_PORT_KEY = 'main'

  String version
  HostMetaModel host
  Map<String, Integer> ports
  Map<String, ConfigMetaModel> configs

  @Override
  int getMainPort()
  {
    getPort(MAIN_PORT_KEY, defaultPort)
  }

  @Override
  int getPort(String portName)
  {
    if(ports.containsKey(portName))
      ports[portName]
    else
      throw new IllegalArgumentException("not a valid portName: [${portName}]")
  }

  int getPort(String portName, int defaultPortValue)
  {
    ports[portName] ?: defaultPortValue
  }

  /**
   * Default implementation returns -1 => override to return a "real" default port
   */
  @Override
  int getDefaultPort()
  {
    return -1
  }

  @Override
  ConfigMetaModel findConfig(String configName)
  {
    configs[configName]
  }

  @Override
  Object toExternalRepresentation()
  {
    def res = [
      version: version,
      host: host.toExternalRepresentation(),
    ]

    if(mainPort != defaultPort)
      res.port = mainPort

    if(ports.size() > 1)
      res.ports =  GluGroovyCollectionUtils.xorMap(ports, [MAIN_PORT_KEY])

    if(configs)
    {
      if(configs.size() == 1)
        res.config = configs.keySet().iterator().next()
      else
        res.configs = configs.keySet().collect { it }
    }

    return res
  }
}