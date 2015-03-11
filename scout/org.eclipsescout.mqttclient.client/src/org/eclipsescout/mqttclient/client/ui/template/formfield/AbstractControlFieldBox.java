/**
 *
 */
package org.eclipsescout.mqttclient.client.ui.template.formfield;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipsescout.mqttclient.shared.ui.template.formfield.AbstractControlFieldBoxData;

/**
 * @author mzi
 */
@FormData(value = AbstractControlFieldBoxData.class, sdkCommand = FormData.SdkCommand.CREATE, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE)
public abstract class AbstractControlFieldBox extends AbstractSequenceBox {

  private AbstractActionButton m_actionButton = null;

  /**
   * @return the Label1Field
   */
  public LabelField getLabelField() {
    return getFieldByClass(LabelField.class);
  }

  /**
   * @return the Message1Field
   */
  public MessageField getMessageField() {
    return getFieldByClass(MessageField.class);
  }

  /**
   * @return the Topic1Field
   */
  public TopicField getTopicField() {
    return getFieldByClass(TopicField.class);
  }

  public AbstractActionButton getActionButton() {
    return m_actionButton;
  }

  public void setActionButton(AbstractActionButton actionButton) {
    m_actionButton = actionButton;
  }

  @Order(10.0)
  public class LabelField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Label");
    }

    @Override
    protected void execChangedValue() throws ProcessingException {
      AbstractActionButton button = getActionButton();

      if (button != null) {
        button.updateLabel();
      }
    }
  }

  @Order(20.0)
  public class TopicField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Topic");
    }
  }

  @Order(30.0)
  public class MessageField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Message");
    }
  }

}
