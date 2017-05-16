package com.simon.router.build.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

/**
 * Created by sunmeng on 17/4/26.
 */
class RouterMasterPlugin implements Plugin<Project> {

    private static final String PLUGIN_NAME = "com.simon.router";

    private static final String OPTION_MODULE = "router_module";
    private static final String OPTION_ROOT = "router_is_root";
    private static final String OPTION_DEPENDENCIES = "router_dependencies";

    @Override
    void apply(Project project) {
        project.afterEvaluate {
            project.tasks.withType(JavaCompile) {
                options.compilerArgs << getArgText(OPTION_MODULE, project.name)
                def root = project.plugins.hasPlugin("com.android.application")
                options.compilerArgs << getArgText(OPTION_ROOT, String.valueOf(root))
                if (root) {
                    options.compilerArgs << getArgText(OPTION_DEPENDENCIES, getDependModules(project.rootProject))
                }
            }
        }
    }

    def getDependModules(Project rootProject) {
        StringBuffer sb = null;
        for (Project sub : rootProject.allprojects) {
            if (!sub.plugins.hasPlugin(PLUGIN_NAME)) {
                continue;
            }
            if (sb == null) {
                sb = new StringBuffer(sub.name)
            } else {
                sb.append(",").append(sub.name);
            }
        }
        return sb == null ? null : sb.toString();
    }

    def getArgText(String name, String value) {
        return String.format("-A%s=%s", name, value);
    }
}