/**
 *
 */
package org.eclipsescout.mqttclient.client.ui.template.formfield;

import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipsescout.mqttclient.client.ui.template.formfield.AbstractSensorBox.LabelField;

/**
 * @author mzi
 */
public abstract class AbstractSensorField extends AbstractStringField {
  public static final String BUTTON_HIDE_LABEL = "--";

  private AbstractSensorBox m_sensorBox = null;

  protected void setSensorFieldBox(AbstractSensorBox sensorBox, AbstractSensorField sensorField) {
    m_sensorBox = sensorBox;

    if (m_sensorBox != null) {
      m_sensorBox.setSensorField(this);
    }
  }

  protected AbstractSensorBox getSensorFieldBox() {
    return m_sensorBox;
  }

  protected void updateLabel() {
    String newLabel = getSensorFieldBox().getFieldByClass(LabelField.class).getValue();

    if (newLabel != null) {
      setLabel(newLabel);
      setVisible(!newLabel.equals(BUTTON_HIDE_LABEL));
    }
  }

//
//  @Override
//  protected void execInitField() throws ProcessingException {
//    setSensorFieldBox(getSensorFieldBox(), this);
//  }

}
