/**
 *
 */
package org.eclipsescout.mqttclient.client.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.service.AbstractService;
import org.eclipsescout.mqttclient.client.ui.desktop.Desktop;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox;

/**
 * @author mzi
 */
public class MessageHandlingService extends AbstractService implements IMessageHandlingService {

  Map<AbstractStringField, String> m_topicMap = new HashMap<>();

  @Override
  public void handleMessage(String topic, String message, byte[] payload, int qos, boolean retained, Date received) throws ProcessingException {
    DesktopForm desktopForm = getDesktopForm();

    if (desktopForm != null) {
      AbstractTable table = desktopForm.getMessagesField().getTable();
      table.addRowByArray(new Object[]{message, topic, received, qos, retained});

      // update all fields that have registered for this topic
      for (AbstractStringField field : getTopicFields(topic)) {
        field.setValue(message);
      }

      // TODO add custom handling for new/special fields here.
      // see examples below
      // else if (topic.equals("/ece/scout/light")) {
      //   desktopForm.getLightField().setValue(message);
      // }
      // else if (topic.equals("image")) {
      //   desktopForm.getSelfieField().setImage(payload);
      // }
    }
  }

  @Override
  public void handleDisconnect() throws ProcessingException {
    DesktopForm desktopForm = getDesktopForm();

    if (desktopForm != null) {
      ConnectionBox box = desktopForm.getConnectionBox();

      if (box != null) {
        box.updateClientFields();
        box.updateConnectionFields();
        box.updateConnectionStatus();
      }

      MessageBox.showOkMessage(TEXTS.get("MQTTClient"), null, TEXTS.get("MqttConnectionLost"));
    }
  }

  private DesktopForm getDesktopForm() {
    if (Desktop.get() == null) {
      return null;
    }
    else {
      return Desktop.get().findForm(DesktopForm.class);
    }
  }

  @Override
  public void addTopicMapEntry(String topic, AbstractStringField field) throws ProcessingException {
    if (StringUtility.hasText(topic) && field != null) {
      m_topicMap.put(field, topic);
    }
  }

  /* (non-Javadoc)
   * @see org.eclipsescout.mqttclient.client.services.IMessageHandlingService#getTopicFields(java.lang.String)
   */
  @Override
  public List<AbstractStringField> getTopicFields(String topic) {
    List<AbstractStringField> fields = new ArrayList<>();

    for (AbstractStringField field : m_topicMap.keySet()) {
      String fieldTopic = m_topicMap.get(field);

      if (fieldTopic.equals(topic)) {
        fields.add(field);
      }
    }

    return fields;
  }
}
