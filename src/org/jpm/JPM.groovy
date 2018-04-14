package org.jpm

class JPM implements Serializable {
  private final steps

  JPM(steps) {
    this.steps = steps
  }

  void install() {
    steps.echo "Installing jenkins scripts..."

    def config = steps.readYaml(file: 'jpm.yml')

    steps.sh "rm -rf jpm_modules"
    steps.sh "mkdir jpm_modules"

    config.dependencies.each { dependency ->
      def parts = dependency.split('@')

      def name = parts[0]
      def version = parts[1] ?: 'latest'

      steps.sh "mkdir jpm_modules/${name}"
      steps.httpRequest(
        url: "http://192.168.1.105:3000/package/${dependency}",
        outputFile: "jpm_modules/${name}/pkg.zip"
      )

      steps.unzip zipFile: "jpm_modules/${name}/pkg.zip", dir: "jpm_modules/${name}/"
    }
  }

  def load(final String name) {
    def config = steps.readYaml(file: "jpm_modules/${name}/jpm.yml")
    def script = config.main ?: 'index.groovy'

    return steps.load("${steps.pwd()}/jpm_modules/${name}/${script}")
  }
}
