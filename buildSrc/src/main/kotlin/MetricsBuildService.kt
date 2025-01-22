import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.kotlin.dsl.listProperty
import javax.inject.Inject


data class MetricValue(
    val metric: String,
    val value: String
)

abstract class MetricsBuildService @Inject constructor(
    objectFactory: ObjectFactory,
    private val providerFactory: ProviderFactory
) : BuildService<BuildServiceParameters.None> {

    private val values: ListProperty<MetricValue> =
        objectFactory.listProperty<MetricValue>()

    fun collectMetric(value: Provider<MetricValue>) {
        synchronized(this) {
            values.add(value)
        }
    }

    fun getAllMetrics(): Provider<List<MetricValue>> {
        return providerFactory.provider {
            readValues()
        }
    }

    private fun readValues(): List<MetricValue> {
        return synchronized(this) {
            values.disallowChanges()
            values.get()
        }
    }

    override fun toString(): String {
        return "MetricsBuildService#${System.identityHashCode(this)}"
    }
}