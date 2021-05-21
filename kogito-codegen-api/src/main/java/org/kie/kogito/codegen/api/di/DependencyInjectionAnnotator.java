/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.codegen.api.di;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;

public interface DependencyInjectionAnnotator {
    <T extends NodeWithAnnotations<?>> T withProduces(T node, boolean isDefault);

    <T extends NodeWithAnnotations<?>> T withNamed(T node, String name);

    <T extends NodeWithAnnotations<?>> T withApplicationComponent(T node);

    <T extends NodeWithAnnotations<?>> T withNamedApplicationComponent(T node, String name);

    <T extends NodeWithAnnotations<?>> T withSingletonComponent(T node);

    <T extends NodeWithAnnotations<?>> T withNamedSingletonComponent(T node, String name);

    <T extends NodeWithAnnotations<?>> T withInjection(T node, boolean forceLazyInit);

    default <T extends NodeWithAnnotations<?>> T withInjection(T node) {
        return withInjection(node, false);
    }

    <T extends NodeWithAnnotations<?>> T withNamedInjection(T node, String name);

    <T extends NodeWithAnnotations<?>> T withOptionalInjection(T node);

    <T extends NodeWithAnnotations<?>> T withIncomingMessage(T node, String channel);

    <T extends NodeWithAnnotations<?>> T withOutgoingMessage(T node, String channel);

    <T extends NodeWithAnnotations<?>> T withConfigInjection(T node, String configKey);

    <T extends NodeWithAnnotations<?>> T withConfigInjection(T node, String configKey, String defaultValue);

    MethodCallExpr withMessageProducer(MethodCallExpr produceMethod, String channel, Expression event);

    default <T extends NodeWithAnnotations<?>> T withSecurityRoles(T node, String[] roles) {
        if (roles != null && roles.length > 0) {
            List<Expression> rolesExpr = new ArrayList<>();

            for (String role : roles) {
                rolesExpr.add(new StringLiteralExpr(role.trim()));
            }

            node.addAnnotation(new SingleMemberAnnotationExpr(new Name("javax.annotation.security.RolesAllowed"), new ArrayInitializerExpr(NodeList.nodeList(rolesExpr))));
        }
        return node;
    }

    String optionalInstanceInjectionType();

    Expression optionalInstanceExists(String fieldName);

    default Expression getOptionalInstance(String fieldName) {
        return new MethodCallExpr(new NameExpr(fieldName), "get");
    }

    String multiInstanceInjectionType();

    Expression getMultiInstance(String fieldName);

    String applicationComponentType();

    String emitterType(String dataType);

    <T extends NodeWithAnnotations<?>> T withEagerStartup(T node);

    <T extends NodeWithAnnotations<?>> T withFactoryClass(T node);

    <T extends NodeWithAnnotations<?>> T withFactoryMethod(T node);
}
