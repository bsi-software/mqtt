/**
 *
 */
package org.eclipsescout.mqttclient.client.ui.template.formfield;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.mqttclient.client.services.IMessageHandlingService;
import org.eclipsescout.mqttclient.client.services.MqttService;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm;
import org.eclipsescout.mqttclient.shared.ui.template.formfield.AbstractSensorBoxData;

/**
 * @author mzi
 */
@FormData(value = AbstractSensorBoxData.class, sdkCommand = FormData.SdkCommand.CREATE, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE)
public abstract class AbstractSensorBox extends AbstractSequenceBox {

  private AbstractSensorField m_sensorField = null;
  private DesktopForm m_desktopForm = null;

  @Override
  protected boolean getConfiguredAutoCheckFromTo() {
    return false;
  }

  /**
   * @return the LabelField
   */
  public LabelField getLabelField() {
    return getFieldByClass(LabelField.class);
  }

  /**
   * @return the SubscribeButton
   */
  public SubscribeButton getSubscribeButton() {
    return getFieldByClass(SubscribeButton.class);
  }

  /**
   * @return the TopicField
   */
  public TopicField getTopicField() {
    return getFieldByClass(TopicField.class);
  }

  public AbstractSensorField getSensorField() {
    return m_sensorField;
  }

  public void setSensorField(AbstractSensorField sensorField) {
    m_sensorField = sensorField;
  }

  public DesktopForm getDesktopForm() {
    return m_desktopForm;
  }

  public void setDesktopForm(DesktopForm desktopForm) {
    m_desktopForm = desktopForm;
  }

  @Order(10.0)
  public class LabelField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Label");
    }

    @Override
    protected void execChangedValue() throws ProcessingException {
      AbstractSensorField sensorField = getSensorField();

      if (sensorField != null) {
        sensorField.updateLabel();
      }
    }
  }

  @Order(20.0)
  public class TopicField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Topic");
    }

    @Override
    protected void execChangedValue() throws ProcessingException {
      getSubscribeButton().setEnabled(StringUtility.hasText(getValue()));
    }
  }

  @Order(30.0)
  public class SubscribeButton extends AbstractButton {

    @Override
    protected boolean getConfiguredEnabled() {
      return false;
    }

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Subscribe");
    }

    @Override
    protected void execClickAction() throws ProcessingException {
      String topic = getTopicField().getValue();

      // subscribe the specified topic
      SERVICES.getService(MqttService.class).subscribe(topic, null);
      ((DesktopForm) getForm()).addTopicRow(topic, null, true);

      // add field to message handling service
      SERVICES.getService(IMessageHandlingService.class).addTopicMapEntry(topic, getSensorField());

      setEnabled(false);
    }
  }
}
