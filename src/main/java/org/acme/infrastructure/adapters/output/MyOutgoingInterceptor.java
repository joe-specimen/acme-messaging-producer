package org.acme.infrastructure.adapters.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.common.annotation.Identifier;
import io.smallrye.reactive.messaging.OutgoingInterceptor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.reactive.messaging.Message;

@Identifier("persons")
@ApplicationScoped
@JBossLog
public class MyOutgoingInterceptor implements OutgoingInterceptor {

   @Inject
   ObjectMapper objectMapper;

   @Override
   public Message<?> beforeMessageSend(Message<?> message) {
      String jsonPayload = null;

      try {
         jsonPayload = objectMapper.writeValueAsString(message.getPayload());
      } catch (JsonProcessingException e) {
         throw new RuntimeException(e);
      }

      return message.withPayload(jsonPayload);
   }

   @Override
   public void onMessageAck(Message<?> message) {
      log.infof("Message: %s", message.getPayload());
   }

   @Override
   public void onMessageNack(Message<?> message, Throwable failure) {

   }
}
