# ── Stage 1: Cache de dependências Gradle ────────────────────────────────────
FROM eclipse-temurin:25-jdk-noble AS deps
WORKDIR /build
COPY gradlew settings.gradle build.gradle ./
COPY gradle/ gradle/
RUN chmod +x gradlew && \
    ./gradlew dependencies --no-daemon --configuration compileClasspath 2>&1 | tail -5

# ── Stage 2: Compilação e extração de layers ─────────────────────────────────
FROM deps AS builder
COPY src/ src/
RUN ./gradlew bootJar --no-daemon -x test && \
    java -Djarmode=layertools \
         -jar build/libs/*.jar \
         extract --destination build/extracted

# ── Stage 3: Runtime mínimo ──────────────────────────────────────────────────
FROM eclipse-temurin:25-jre-noble AS runtime

RUN groupadd --system appgroup && \
    useradd --system --gid appgroup --no-create-home appuser

WORKDIR /app

# Layers copiadas em ordem crescente de volatilidade para maximizar cache
COPY --from=builder --chown=appuser:appgroup /build/extracted/dependencies/          ./
COPY --from=builder --chown=appuser:appgroup /build/extracted/spring-boot-loader/    ./
COPY --from=builder --chown=appuser:appgroup /build/extracted/snapshot-dependencies/ ./
COPY --from=builder --chown=appuser:appgroup /build/extracted/application/           ./

USER appuser

# HTTP (Spring WebMVC) + gRPC
EXPOSE 8080 9090

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "org.springframework.boot.loader.launch.JarLauncher"]