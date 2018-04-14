import org.jpm.JPM
import groovy.transform.Field

@Field
def jpmInstance = new JPM(this)

def install() {
    jpmInstance.install()
}

def call(final String name) {
    return jpmInstance.load(name)
}
