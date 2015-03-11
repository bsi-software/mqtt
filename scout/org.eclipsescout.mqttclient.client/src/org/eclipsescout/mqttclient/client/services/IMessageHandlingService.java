/**
 *
 */
package org.eclipsescout.mqttclient.client.services;

import java.util.Date;
import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.service.IService;

/**
 * @author mzi
 */
public interface IMessageHandlingService extends IService {

  /**
   * @param topic
   * @param message
   * @param payload
   * @param qos
   * @param retained
   * @param received
   * @throws ProcessingException
   */
  void handleMessage(String topic, String message, byte[] payload, int qos, boolean retained, Date received) throws ProcessingException;

  /**
   * @throws org.eclipse.scout.commons.exception.ProcessingException
   */
  void handleDisconnect() throws ProcessingException;

  List<AbstractStringField> getTopicFields(String topic);

  /**
   * @param topic
   * @param field
   * @throws org.eclipse.scout.commons.exception.ProcessingException
   */
  void addTopicMapEntry(String topic, AbstractStringField field) throws ProcessingException;
}
