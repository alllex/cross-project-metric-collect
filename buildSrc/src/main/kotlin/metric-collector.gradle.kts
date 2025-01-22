import java.util.*


val projectName = project.name
val metricValue = UUID.randomUUID().toString()

gradle.sharedServices.registerIfAbsent("metrics", MetricsBuildService::class.java)
    .get()
    .also { println("Using metrics service for collecting: $it") }
    .collectMetric(provider {
        MetricValue("metric:$projectName", metricValue)
    })