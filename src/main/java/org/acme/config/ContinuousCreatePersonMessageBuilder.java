package org.acme.config;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.jbosslog.JBossLog;
import org.acme.infrastructure.adapters.output.CreatePersonMessageEmitter;
import org.acme.infrastructure.shared.CreatePersonDTO;

@ApplicationScoped
@JBossLog
public class ContinuousCreatePersonMessageBuilder {

   @Inject
   CreatePersonMessageEmitter createPersonMessageProducer;

   private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
       1,
       Thread.ofVirtual().factory()
   );

   void onStart(@Observes StartupEvent ev) {
      scheduler.scheduleAtFixedRate(
          () -> sendMessage(),
          0,
          1,
          TimeUnit.SECONDS
      );
   }

   void onStop(@Observes ShutdownEvent ev) {
      scheduler.shutdown();
   }

   private void sendMessage() {
      var createPersonDto = new CreatePersonDTO(
          "Name " + System.currentTimeMillis(),
          10
      );
      log.info("Sending message");
      createPersonMessageProducer.produce(createPersonDto);
      log.info("Message sent");
   }

}
