/**
 *
 */
package org.eclipsescout.mqttclient.client.ui.template.formfield;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.mqttclient.client.services.MqttService;
import org.eclipsescout.mqttclient.client.ui.template.formfield.AbstractControlFieldBox.LabelField;

/**
 * @author mzi
 */
public abstract class AbstractActionButton extends AbstractButton {
  public static final String BUTTON_HIDE_LABEL = "--";

  private AbstractControlFieldBox m_controlFieldBox = null;

  protected void setControlFieldBox(AbstractControlFieldBox box, AbstractActionButton button) {
    m_controlFieldBox = box;

    if (m_controlFieldBox != null) {
      m_controlFieldBox.setActionButton(this);
    }
  }

  protected AbstractControlFieldBox getControlFieldBox() {
    return m_controlFieldBox;
  }

  protected void updateLabel() {
    String newLabel = getControlFieldBox().getFieldByClass(LabelField.class).getValue();

    if (newLabel != null) {
      setLabel(newLabel);
      setVisible(!newLabel.equals(BUTTON_HIDE_LABEL));
    }
  }

  @Override
  protected void execClickAction() throws ProcessingException {
    SERVICES.getService(MqttService.class).publish(getControlFieldBox().getTopicField().getValue(), getControlFieldBox().getMessageField().getValue(), 1, false);
  }

}
