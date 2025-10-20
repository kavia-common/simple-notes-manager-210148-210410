This directory contains Checkstyle configuration used by CI to run checkstyleMain/checkstyleTest.
- The build enables the Checkstyle plugin.
- Violations are warnings and do not fail the build (ignoreFailures=true) but allow the task to exist for CI scripts that invoke it.
