package projektor.metrics

import io.ktor.util.KtorExperimentalAPI
import io.micrometer.core.instrument.Clock
import io.micrometer.influx.InfluxConfig
import io.micrometer.influx.InfluxMeterRegistry
import java.time.Duration

@KtorExperimentalAPI
fun createRegistry(config: InfluxMetricsConfig): InfluxMeterRegistry {

    val registryConfig: InfluxConfig = object : InfluxConfig {
        override fun step(): Duration {
            return Duration.ofSeconds(config.interval)
        }

        override fun db(): String {
            return config.dbName
        }

        override fun get(k: String): String? {
            return when (k) {
                "influx.autoCreateDb" -> config.autoCreateDb.toString()
                "influx.enabled" -> config.enabled.toString()
                "influx.uri" -> config.uri
                else -> null
            }
        }
    }

    return InfluxMeterRegistry(registryConfig, Clock.SYSTEM)
}