package org.acme.infrastructure.adapters.output;

import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;
import org.acme.application.ports.output.messaging.MessageEmitter;
import org.acme.infrastructure.shared.CreatePersonDTO;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@JBossLog
public class CreatePersonMessageEmitter implements MessageEmitter<CreatePersonDTO> {

   @Inject
   @Channel("persons")
   Emitter<CreatePersonDTO> createPersonEmitter;

   @Override
   public void produce(CreatePersonDTO payload) {
      createPersonEmitter.send(payload).whenComplete(
          (unused, throwable) -> {
             if (throwable != null) {
                log.errorf("Error sending create person: %s", throwable.getMessage());
                return;
             }
             log.infof("Sending create person SUCC: %s", payload);
          }
      );
   }

}
