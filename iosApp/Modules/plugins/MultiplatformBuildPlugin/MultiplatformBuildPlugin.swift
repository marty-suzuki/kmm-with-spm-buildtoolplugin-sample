import PackagePlugin
import Foundation

@main
struct MultiplatformBuildPlugin: BuildToolPlugin {
    func createBuildCommands(
        context: PluginContext,
        target: Target
    ) async throws -> [Command] {
        let projectRoot = context.package.directory.appending(["..", ".."])
        let executable = projectRoot.appending(["gradlew"])

        return [
            .prebuildCommand(
                displayName: "MultiplatformBuildPlugin",
                executable: executable,
                arguments: [
                    "-p",
                    projectRoot.string,
                    "assembleReleaseXCFrameworkForSPM"
                ],
                environment: [
                    /* ⚠️ Must set JAVA_HOME absolute path! ⚠️ */
                    "JAVA_HOME": ""
                ],
                outputFilesDirectory: context.pluginWorkDirectory
            )
        ]
    }
}
