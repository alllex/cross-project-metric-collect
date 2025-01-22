plugins {
    `metric-collector`
}

val metricsService =
    gradle.sharedServices.registerIfAbsent("metrics", MetricsBuildService::class.java)
        .get()

interface GetServices { @get:Inject val flowScope: FlowScope }
val flowScope = objects.newInstance(GetServices::class).flowScope

flowScope.always(MetricsAction::class) {
    parameters {
        configurationTimeMetrics.addAll(metricsService.getAllMetrics())
    }
}

abstract class MetricsAction : FlowAction<MetricsAction.Params> {
    interface Params : FlowParameters {
        @get:Input
        val configurationTimeMetrics: ListProperty<MetricValue>
    }

    override fun execute(parameters: Params) {
        println("Metrics action:")
        val configTimeMetrics = parameters.configurationTimeMetrics.get()
        println("Configuration time metrics: (${configTimeMetrics.size})")
        for (configTimeMetric in configTimeMetrics) {
            println("${configTimeMetric.metric}: ${configTimeMetric.value}")
        }
    }
}
