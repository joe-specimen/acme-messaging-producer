package org.acme.application.ports.output.messaging;

public interface MessageEmitter<T> {

   void produce(T payload);

}
