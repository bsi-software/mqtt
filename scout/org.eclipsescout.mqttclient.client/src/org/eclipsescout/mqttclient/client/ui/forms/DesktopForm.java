/**
 *
 */
package org.eclipsescout.mqttclient.client.ui.forms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TreeMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.ValueFieldMenuType;
import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateTimeColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractIntegerColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.client.ui.form.fields.checkbox.AbstractCheckBox;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.integerfield.AbstractIntegerField;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.extension.client.ui.action.menu.AbstractExtensibleMenu;
import org.eclipse.scout.rt.extension.client.ui.basic.table.AbstractExtensibleTable;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.mqttclient.client.services.MqttService;
import org.eclipsescout.mqttclient.client.services.UserPreferencesService;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.BrokerURLField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.ClientIdField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.LastWillAndTestamentBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.LastWillAndTestamentBox.WillMessageField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.LastWillAndTestamentBox.WillQoSField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.LastWillAndTestamentBox.WillRetainedField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.LastWillAndTestamentBox.WillTopicField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.PublishParametersBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.PublishParametersBox.DefaultQoSField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.PublishParametersBox.DefaultRetainedField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.PublishParametersBox.DefaultTopicField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.ShowConnectionConfigurationField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.StatusBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.StatusBox.ConnectButton;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.StatusBox.DisconnectButton;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.StatusBox.StatusField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.UserBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.UserBox.CleanSessionField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.UserBox.ConnectionTimeoutField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.UserBox.HidePasswordField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.UserBox.MaskedPasswordField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.UserBox.PasswordField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ConnectionBox.UserBox.UserNameField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.ActionButtonsBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.ActionButtonsBox.Action1Button;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.ActionButtonsBox.Action2Button;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.ActionButtonsBox.Action3Button;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.ConfigureActionsBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.ConfigureActionsBox.Control1Box;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.ConfigureActionsBox.Control2Box;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.ConfigureActionsBox.Control3Box;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.ConfigureSensorsBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.ConfigureSensorsBox.Sensor1Box;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.ConfigureSensorsBox.Sensor2Box;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.ConfigureSensorsBox.Sensor3Box;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.Sensor1Field;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.Sensor2Field;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.Sensor3Field;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.ControlBox.ShowConfigurationField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.MessagesBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.MessagesBox.MessagesField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.MessagesBox.PublishBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.MessagesBox.PublishBox.MessageField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.MessagesBox.PublishBox.PublishButton;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.MessagesBox.PublishBox.TopicField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.SubscriptionsBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.SubscriptionsBox.SubscribeBox;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.SubscriptionsBox.SubscribeBox.SubscribeButton;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.SubscriptionsBox.SubscribeBox.TopicFilterField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.SubscriptionsBox.SubscribeBox.TopicQoSField;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.SubscriptionsBox.SubscribeBox.UnsubscribeButton;
import org.eclipsescout.mqttclient.client.ui.forms.DesktopForm.MainBox.DesktopBox.SubscriptionsBox.SubscriptionsField;
import org.eclipsescout.mqttclient.client.ui.template.formfield.AbstractActionButton;
import org.eclipsescout.mqttclient.client.ui.template.formfield.AbstractControlFieldBox;
import org.eclipsescout.mqttclient.client.ui.template.formfield.AbstractSensorBox;
import org.eclipsescout.mqttclient.client.ui.template.formfield.AbstractSensorField;
import org.eclipsescout.mqttclient.shared.Icons;

/**
 * @author mzi
 */
@FormData(sdkCommand = FormData.SdkCommand.IGNORE)
public class DesktopForm extends AbstractForm {

  /**
   * @throws org.eclipse.scout.commons.exception.ProcessingException
   */
  public DesktopForm() throws ProcessingException {
    super();
  }

  @Override
  protected boolean getConfiguredAskIfNeedSave() {
    return false;
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return DISPLAY_HINT_VIEW;
  }

  @Override
  protected String getConfiguredDisplayViewId() {
    return VIEW_ID_CENTER;
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.EclipseScout;
  }

  /**
   * save current mqtt parameters to preferences
   */
  @Override
  protected void execDisposeForm() throws ProcessingException {
    UserPreferencesService prefs = SERVICES.getService(UserPreferencesService.class);

    if (prefs != null) {
      prefs.setBrokerUrl(getBrokerURLField().getValue());
      prefs.setClientId(getClientIdField().getValue());
      prefs.setUserName(getUserNameField().getValue());
      prefs.setPassword(getPasswordField().getValue());
      prefs.setConnectionTimeout(getConnectionTimeoutField().getValue().toString());
      prefs.setCleanSession(getCleanSessionField().getValue().toString());
      prefs.setWillTopic(getWillTopicField().getValue());
      prefs.setWillMessage(getWillMessageField().getValue());
      prefs.setWillQos(getWillQoSField().getValue().toString());
      prefs.setWillRetained(getWillRetainedField().getValue().toString());
      prefs.setDefaultTopic(getDefaultTopicField().getValue());
      prefs.setDefaultQos(getDefaultQoSField().getValue().toString());
      prefs.setDefaultRetained(getDefaultRetainedField().getValue().toString());

      prefs.store();
    }
  }

  /**
   * load mqtt parameters to preferences
   */
  @Override
  protected void execInitForm() throws ProcessingException {
    super.execInitForm();

    UserPreferencesService prefs = SERVICES.getService(UserPreferencesService.class);

    // set parameters for current demo
    String baseTopic = "/eclipse/scout/arduino/";

    getControl1Box().getLabelField().setValue(" ON ");
    getControl1Box().getTopicField().setValue(baseTopic);
    getControl1Box().getMessageField().setValue("RELAIS ON");

    getControl2Box().getLabelField().setValue(" OFF ");
    getControl2Box().getTopicField().setValue(baseTopic);
    getControl2Box().getMessageField().setValue("RELAIS OFF");

    getControl3Box().getLabelField().setValue(" LDR ");
    getControl3Box().getTopicField().setValue(baseTopic);
    getControl3Box().getMessageField().setValue("LDR GET");

    getSensor1Box().getLabelField().setValue("LDR Value");
    getSensor1Box().getTopicField().setValue(baseTopic + "ldr");
    getSensor2Box().getLabelField().setValue("--");
    getSensor3Box().getLabelField().setValue("--");

    try {
      prefs.load();

      getBrokerURLField().setValue(prefs.getBrokerUrl());
      getClientIdField().setValue(prefs.getClientId());

      getUserNameField().setValue(prefs.getUserName());
      getPasswordField().setValue(prefs.getPassword());
      getConnectionTimeoutField().setValue(new Integer(prefs.getConnectionTimeout()));
      getCleanSessionField().setValue(new Boolean(prefs.getCleanSession()));

      getWillTopicField().setValue(prefs.getWillTopic());
      getWillMessageField().setValue(prefs.getWillMessage());
      getWillQoSField().setValue(new Integer(prefs.getWillQos()));
      getWillRetainedField().setValue(new Boolean(prefs.getWillRetained()));

      getDefaultTopicField().setValue(prefs.getDefaultTopic());
      getDefaultQoSField().setValue(new Integer(prefs.getDefaultQos()));
      getDefaultRetainedField().setValue(new Boolean(prefs.getDefaultRetained()));
    }
    catch (Exception e) {
      MessageBox.showOkMessage(TEXTS.get("MQTTClient"), TEXTS.get("PrefExeption"), e.getMessage());
    }

    getDesktopBox().setSelectedTab(getConnectionBox());
  }

  /**
   * @throws org.eclipse.scout.commons.exception.ProcessingException
   */
  public void startView() throws ProcessingException {
    startInternal(new ViewHandler());
  }

  /**
   * @return the BrokerURLField
   */
  public BrokerURLField getBrokerURLField() {
    return getFieldByClass(BrokerURLField.class);
  }

  /**
   * @return the CleanSessionField
   */
  public CleanSessionField getCleanSessionField() {
    return getFieldByClass(CleanSessionField.class);
  }

//  /**
//   * @return the ClientBox
//   */
//  public ClientBox getClientBox() {
//    return getFieldByClass(ClientBox.class);
//  }

  /**
   * @return the ClientIdField
   */
  public ClientIdField getClientIdField() {
    return getFieldByClass(ClientIdField.class);
  }

  /**
   * @return the ConfigureActionsBox
   */
  public ConfigureActionsBox getConfigureActionsBox() {
    return getFieldByClass(ConfigureActionsBox.class);
  }

  /**
   * @return the ConfigureSensorsBox
   */
  public ConfigureSensorsBox getConfigureSensorsBox() {
    return getFieldByClass(ConfigureSensorsBox.class);
  }

  /**
   * @return the ConnectButton
   */
  public ConnectButton getConnectButton() {
    return getFieldByClass(ConnectButton.class);
  }

  /**
   * @return the ConnectionBox
   */
  public ConnectionBox getConnectionBox() {
    return getFieldByClass(ConnectionBox.class);
  }

  /**
   * @return the ConnectionTimeoutField
   */
  public ConnectionTimeoutField getConnectionTimeoutField() {
    return getFieldByClass(ConnectionTimeoutField.class);
  }

  /**
   * @return the DesktopBox
   */
  public DesktopBox getDesktopBox() {
    return getFieldByClass(DesktopBox.class);
  }

  /**
   * @return the getControl1Box
   */
  public Control1Box getControl1Box() {
    return getFieldByClass(Control1Box.class);
  }

  /**
   * @return the getControl1Box
   */
  public Control2Box getControl2Box() {
    return getFieldByClass(Control2Box.class);
  }

  /**
   * @return the getControl1Box
   */
  public Control3Box getControl3Box() {
    return getFieldByClass(Control3Box.class);
  }

  /**
   * @return the Action1Button
   */
  public Action1Button getAction1Button() {
    return getFieldByClass(Action1Button.class);
  }

  /**
   * @return the Action2Button
   */
  public Action2Button getAction2Button() {
    return getFieldByClass(Action2Button.class);
  }

  /**
   * @return the Action3Button
   */
  public Action3Button getAction3Button() {
    return getFieldByClass(Action3Button.class);
  }

  /**
   * @return the ControlBox
   */
  public ControlBox getControlBox() {
    return getFieldByClass(ControlBox.class);
  }

  /**
   * @return the ActionButtonsBox
   */
  public ActionButtonsBox getActionButtonsBox() {
    return getFieldByClass(ActionButtonsBox.class);
  }

  /**
   * @return the DisconnectButton
   */
  public DisconnectButton getDisconnectButton() {
    return getFieldByClass(DisconnectButton.class);
  }

  /**
   * @return the HidePasswordField
   */
  public HidePasswordField getHidePasswordField() {
    return getFieldByClass(HidePasswordField.class);
  }

  /**
   * @return the LastWillAndTestamentBox
   */
  public LastWillAndTestamentBox getLastWillAndTestamentBox() {
    return getFieldByClass(LastWillAndTestamentBox.class);
  }

  /**
   * @return the MainBox
   */
  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  /**
   * @return the WillMessageField
   */
  public WillMessageField getWillMessageField() {
    return getFieldByClass(WillMessageField.class);
  }

  /**
   * @return the UnsubscribeButton
   */
  public UnsubscribeButton getUnsubscribeButton() {
    return getFieldByClass(UnsubscribeButton.class);
  }

  /**
   * @return the UserBox
   */
  public UserBox getUserBox() {
    return getFieldByClass(UserBox.class);
  }

  /**
   * @return the MessageField
   */
  public MessageField getMessageField() {
    return getFieldByClass(MessageField.class);
  }

  /**
   * @return the MessagesBox
   */
  public MessagesBox getMessagesBox() {
    return getFieldByClass(MessagesBox.class);
  }

  /**
   * @return the MessagesField
   */
  public MessagesField getMessagesField() {
    return getFieldByClass(MessagesField.class);
  }

  /**
   * @return the PasswordField
   */
  public PasswordField getPasswordField() {
    return getFieldByClass(PasswordField.class);
  }

  /**
   * @return the WillQoSField
   */
  public WillQoSField getWillQoSField() {
    return getFieldByClass(WillQoSField.class);
  }

  /**
   * @return the WillRetainedField
   */
  public WillRetainedField getWillRetainedField() {
    return getFieldByClass(WillRetainedField.class);
  }

  /**
   * @return the WillTopicField
   */
  public WillTopicField getWillTopicField() {
    return getFieldByClass(WillTopicField.class);
  }

  /**
   * @return the PublishBox
   */
  public PublishBox getPublishBox() {
    return getFieldByClass(PublishBox.class);
  }

  /**
   * @return the PublishButton
   */
  public PublishButton getPublishButton() {
    return getFieldByClass(PublishButton.class);
  }

  /**
   * @return the PublishParametersBox
   */
  public PublishParametersBox getPublishParametersBox() {
    return getFieldByClass(PublishParametersBox.class);
  }

  /**
   * @return the DefaultQoSField
   */
  public DefaultQoSField getDefaultQoSField() {
    return getFieldByClass(DefaultQoSField.class);
  }

  /**
   * @return the MaskedPasswordField
   */
  public MaskedPasswordField getMaskedPasswordField() {
    return getFieldByClass(MaskedPasswordField.class);
  }

  /**
   * @return the SubscribeBox
   */
  public SubscribeBox getSubscribeBox() {
    return getFieldByClass(SubscribeBox.class);
  }

  /**
   * @return the DefaultRetainedField
   */
  public DefaultRetainedField getDefaultRetainedField() {
    return getFieldByClass(DefaultRetainedField.class);
  }

  /**
   * @return the Sensor1Box
   */
  public Sensor1Box getSensor1Box() {
    return getFieldByClass(Sensor1Box.class);
  }

  /**
   * @return the Sensor1Box
   */
  public Sensor2Box getSensor2Box() {
    return getFieldByClass(Sensor2Box.class);
  }

  /**
   * @return the Sensor1Box
   */
  public Sensor3Box getSensor3Box() {
    return getFieldByClass(Sensor3Box.class);
  }

  /**
   * @return the Sensor1Field
   */
  public Sensor1Field getSensor1Field() {
    return getFieldByClass(Sensor1Field.class);
  }

  /**
   * @return the Sensor2Field
   */
  public Sensor2Field getSensor2Field() {
    return getFieldByClass(Sensor2Field.class);
  }

  /**
   * @return the Sensor1Field
   */
  public Sensor3Field getSensor3Field() {
    return getFieldByClass(Sensor3Field.class);
  }

  /**
   * @return the ShowConfigurationField
   */
  public ShowConfigurationField getShowConfigurationField() {
    return getFieldByClass(ShowConfigurationField.class);
  }

  /**
   * @return the ShowConnectionConfigurationField
   */
  public ShowConnectionConfigurationField getShowConnectionConfigurationField() {
    return getFieldByClass(ShowConnectionConfigurationField.class);
  }

  /**
   * @return the StatusBox
   */
  public StatusBox getStatusBox() {
    return getFieldByClass(StatusBox.class);
  }

  /**
   * @return the StatusField
   */
  public StatusField getStatusField() {
    return getFieldByClass(StatusField.class);
  }

  /**
   * @return the SubscriptionsBox
   */
  public SubscriptionsBox getSubscriptionsBox() {
    return getFieldByClass(SubscriptionsBox.class);
  }

  /**
   * @return the SubscribeButton
   */
  public SubscribeButton getSubscribeButton() {
    return getFieldByClass(SubscribeButton.class);
  }

  /**
   * @return the SubscriptionsField
   */
  public SubscriptionsField getSubscriptionsField() {
    return getFieldByClass(SubscriptionsField.class);
  }

  /**
   * @return the DefaultTopicField
   */
  public DefaultTopicField getDefaultTopicField() {
    return getFieldByClass(DefaultTopicField.class);
  }

  /**
   * @return the TopicField
   */
  public TopicField getTopicField() {
    return getFieldByClass(TopicField.class);
  }

  /**
   * @return the TopicFilterField
   */
  public TopicFilterField getTopicFilterField() {
    return getFieldByClass(TopicFilterField.class);
  }

  /**
   * @return the TopicQoSField
   */
  public TopicQoSField getTopicQoSField() {
    return getFieldByClass(TopicQoSField.class);
  }

  /**
   * @return the UserNameField
   */
  public UserNameField getUserNameField() {
    return getFieldByClass(UserNameField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected boolean getConfiguredLabelVisible() {
      return false;
    }

    @Order(20.0)
    public class DesktopBox extends AbstractTabBox {

      @Order(10.0)
      public class ControlBox extends AbstractGroupBox {

        @Override
        protected int getConfiguredGridColumnCount() {
          return 1;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Control");
        }

        @Order(10.0)
        public class ActionButtonsBox extends AbstractSequenceBox {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Actions");
          }

          @Order(20.0)
          public class Action1Button extends AbstractActionButton {

            @Override
            protected void execInitField() throws ProcessingException {
              setControlFieldBox(getControl1Box(), this);
            }
          }

          @Order(30.0)
          public class Action2Button extends AbstractActionButton {

            @Override
            protected void execInitField() throws ProcessingException {
              setControlFieldBox(getControl2Box(), this);
            }
          }

          @Order(40.0)
          public class Action3Button extends AbstractActionButton {

            @Override
            protected void execInitField() throws ProcessingException {
              setControlFieldBox(getControl3Box(), this);
            }
          }

        }

        @Order(30.0)
        public class Sensor1Field extends AbstractSensorField {

          @Override
          protected void execInitField() throws ProcessingException {
            setSensorFieldBox(getSensor1Box(), this);
          }
        }

        @Order(40.0)
        public class Sensor2Field extends AbstractSensorField {

          @Override
          protected void execInitField() throws ProcessingException {
            setSensorFieldBox(getSensor2Box(), this);
          }
        }

        @Order(50.0)
        public class Sensor3Field extends AbstractSensorField {

          @Override
          protected void execInitField() throws ProcessingException {
            setSensorFieldBox(getSensor3Box(), this);
          }
        }

        @Order(60.0)
        public class ShowConfigurationField extends AbstractCheckBox {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("ShowConfiguration");
          }

          @Override
          protected void execChangedValue() throws ProcessingException {
            getConfigureActionsBox().setVisible(getValue());
            getConfigureSensorsBox().setVisible(getValue());
          }
        }

        @Order(70.0)
        public class ConfigureActionsBox extends AbstractGroupBox {

          @Override
          protected int getConfiguredGridColumnCount() {
            return 1;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("ActionConfiguration");
          }

          @Override
          protected boolean getConfiguredVisible() {
            return false;
          }

          @Order(10.0)
          public class Control1Box extends AbstractControlFieldBox {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Action1");
            }
          }

          @Order(20.0)
          public class Control2Box extends AbstractControlFieldBox {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Action2");
            }
          }

          @Order(30.0)
          public class Control3Box extends AbstractControlFieldBox {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Action3");
            }
          }
        }

        @Order(80.0)
        public class ConfigureSensorsBox extends AbstractGroupBox {

          @Override
          protected int getConfiguredGridColumnCount() {
            return 1;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("ConfigureSensors");
          }

          @Override
          protected boolean getConfiguredVisible() {
            return false;
          }

          @Order(10.0)
          public class Sensor1Box extends AbstractSensorBox {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Sensor1");
            }
          }

          @Order(20.0)
          public class Sensor2Box extends AbstractSensorBox {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Sensor2");
            }
          }

          @Order(30.0)
          public class Sensor3Box extends AbstractSensorBox {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Sensor3");
            }
          }
        }
      }

      @Order(90.0)
      public class ConnectionBox extends AbstractGroupBox {

        @Override
        protected int getConfiguredGridColumnCount() {
          return 1;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Connection");
        }

        public void updateClientFields() throws ProcessingException {
          boolean connected = getMqttService().isConnected();

          getBrokerURLField().setEnabled(!connected);
          getClientIdField().setEnabled(!connected);
        }

        public void updateConnectionFields() throws ProcessingException {
          boolean connected = getMqttService().isConnected();

          getUserBox().setEnabled(!connected);
          getHidePasswordField().setEnabled(true);
          getLastWillAndTestamentBox().setEnabled(!connected);
        }

        public void updateConnectionStatus() throws ProcessingException {
          boolean connected = getMqttService().isConnected();

          if (connected) {
            getStatusField().setValue(TEXTS.get("Connected"));
          }
          else {
            getStatusField().setValue(TEXTS.get("Disconnected"));
          }

          getConnectButton().setVisible(!connected);
          getConnectButton().setEnabled(!connected);
          getDisconnectButton().setVisible(connected);
          getDisconnectButton().setEnabled(connected);
        }

        @Order(40.0)
        public class ShowConnectionConfigurationField extends AbstractCheckBox {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("ShowConfiguration");
          }

          @Override
          protected void execChangedValue() throws ProcessingException {
            getUserBox().setVisible(getValue());
            getLastWillAndTestamentBox().setVisible(getValue());
            getPublishParametersBox().setVisible(getValue());
          }
        }

        @Order(50.0)
        public class UserBox extends AbstractGroupBox {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Connection");
          }

          @Override
          protected boolean getConfiguredVisible() {
            return false;
          }

          @Override
          protected void execInitField() throws ProcessingException {
            updatePasswordVisibility();
          }

          private void updatePasswordVisibility() {
            boolean hidePassword = getHidePasswordField().getValue();

            if (hidePassword) {
              getMaskedPasswordField().setValue(getPasswordField().getValue());
            }
            else {
              getPasswordField().setValue(getMaskedPasswordField().getValue());
            }

            getMaskedPasswordField().setVisible(hidePassword);
            getPasswordField().setVisible(!hidePassword);
          }

          @Order(10.0)
          public class UserNameField extends AbstractStringField {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("UserName");
            }
          }

          @Order(20.0)
          public class PasswordField extends AbstractStringField {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Password");
            }
          }

          @Order(30.0)
          public class MaskedPasswordField extends AbstractStringField {

            @Override
            protected boolean getConfiguredInputMasked() {
              return true;
            }

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Password");
            }
          }

          @Order(40.0)
          public class ConnectionTimeoutField extends AbstractIntegerField {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("ConnectionTimeout");
            }
          }

          @Order(50.0)
          public class CleanSessionField extends AbstractCheckBox {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("CleanSession");
            }
          }

          @Order(60.0)
          public class HidePasswordField extends AbstractCheckBox {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("HidePassword");
            }

            @Override
            protected void execChangedValue() throws ProcessingException {
              updatePasswordVisibility();
            }
          }

        }

        @Order(60.0)
        public class LastWillAndTestamentBox extends AbstractGroupBox {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("LastWillAndTestament");
          }

          @Override
          protected boolean getConfiguredVisible() {
            return false;
          }

          @Order(10.0)
          public class WillTopicField extends AbstractStringField {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Topic");
            }
          }

          @Order(20.0)
          public class WillMessageField extends AbstractStringField {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Message");
            }
          }

          @Order(30.0)
          public class WillQoSField extends AbstractIntegerField {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("QoS");
            }
          }

          @Order(40.0)
          public class WillRetainedField extends AbstractCheckBox {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Retained");
            }
          }
        }

//        @Order(20.0)
//        public class ClientBox extends AbstractGroupBox {

//        @Override
//        protected String getConfiguredLabel() {
//          return TEXTS.get("Client");
//        }

        @Order(20.0)
        public class BrokerURLField extends AbstractStringField {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("BrokerURL");
          }
        }

        @Order(30.0)
        public class ClientIdField extends AbstractStringField {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("ClientId");
          }
        }

//        }

        @Order(70.0)
        public class PublishParametersBox extends AbstractGroupBox {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("PublishParameters");
          }

          @Override
          protected boolean getConfiguredVisible() {
            return false;
          }

          @Order(10.0)
          public class DefaultTopicField extends AbstractStringField {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("DefaultTopic");
            }

            @Override
            protected String execValidateValue(String rawValue) throws ProcessingException {
              if (StringUtility.isNullOrEmpty(rawValue)) {
                throw new ProcessingException(TEXTS.get("topicFieldEmpty"));
              }

              return rawValue;
            }
          }

          @Order(20.0)
          public class DefaultQoSField extends AbstractIntegerField {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("QoS");
            }

            @Override
            protected Integer getConfiguredMaxValue() {
              return 2;
            }

            @Override
            protected Integer getConfiguredMinValue() {
              return 0;
            }
          }

          @Order(30.0)
          public class DefaultRetainedField extends AbstractCheckBox {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Retained");
            }
          }
        }

        @Order(10.0)
        public class StatusBox extends AbstractSequenceBox {

          @Override
          protected boolean getConfiguredAutoCheckFromTo() {
            return false;
          }

          @Override
          protected int getConfiguredGridW() {
            return 2;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("ClientStatus");
          }

          @Override
          protected void execInitField() throws ProcessingException {
            updateConnectionStatus();
          }

          @Order(20.0)
          public class StatusField extends AbstractStringField {

            @Override
            protected boolean getConfiguredEnabled() {
              return false;
            }

            @Override
            protected boolean getConfiguredLabelVisible() {
              return false;
            }
          }

          @Order(30.0)
          public class ConnectButton extends AbstractButton {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Connect");
            }

            @Override
            protected void execClickAction() throws ProcessingException {
              getMqttService().setup(
                  getBrokerURLField().getValue(),
                  getClientIdField().getValue()
                  );

              getMqttService().connect(
                  getUserNameField().getValue(),
                  getPasswordField().getValue(),
                  getCleanSessionField().getValue(),
                  getConnectionTimeoutField().getValue(),
                  getWillTopicField().getValue(),
                  getWillMessageField().getValue(),
                  getWillQoSField().getValue(),
                  getWillRetainedField().getValue()
                  );

              updateClientFields();
              updateConnectionFields();
              updateConnectionStatus();
            }
          }

          @Order(40.0)
          public class DisconnectButton extends AbstractButton {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Disconnect");
            }

            @Override
            protected void execClickAction() throws ProcessingException {
              getMqttService().disconnect();

              updateClientFields();
              updateConnectionFields();
              updateConnectionStatus();

              if (getCleanSessionField().isChecked()) {
                getSubscriptionsField().setTopicRowsActiveFlag(false);
              }
            }
          }
        }

      }

      @Order(30.0)
      public class MessagesBox extends AbstractGroupBox {

        @Override
        protected int getConfiguredGridColumnCount() {
          return 1;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Messages");
        }

        @Order(20.0)
        public class MessagesField extends AbstractTableField<MessagesField.Table> {

          @Override
          protected int getConfiguredGridH() {
            return 5;
          }

          @Override
          protected boolean getConfiguredLabelVisible() {
            return false;
          }

          @Order(10.0)
          public class Table extends AbstractExtensibleTable {

            /**
             * @return the TopicColumn
             */
            public TopicColumn getTopicColumn() {
              return getColumnSet().getColumnByClass(TopicColumn.class);
            }

            /**
             * @return the QoSColumn
             */
            public QoSColumn getQoSColumn() {
              return getColumnSet().getColumnByClass(QoSColumn.class);
            }

            /**
             * @return the RetainedColumn
             */
            public RetainedColumn getRetainedColumn() {
              return getColumnSet().getColumnByClass(RetainedColumn.class);
            }

            @Override
            protected void execRowAction(ITableRow row) throws ProcessingException {
              getMenu(ReplyMenu.class).execAction();
              getMessageField().requestFocus();
            }

            /**
             * @return the MessageColumn
             */
            public MessageColumn getMessageColumn() {
              return getColumnSet().getColumnByClass(MessageColumn.class);
            }

            /**
             * @return the ReceivedColumn
             */
            public ReceivedColumn getReceivedColumn() {
              return getColumnSet().getColumnByClass(ReceivedColumn.class);
            }

            @Order(10.0)
            public class MessageColumn extends AbstractStringColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return TEXTS.get("Message");
              }

              @Override
              protected int getConfiguredWidth() {
                return 400;
              }
            }

            @Order(20.0)
            public class TopicColumn extends AbstractStringColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return TEXTS.get("Topic");
              }

              @Override
              protected int getConfiguredWidth() {
                return 250;
              }
            }

            @Order(30.0)
            public class ReceivedColumn extends AbstractDateTimeColumn {

              @Override
              protected String getConfiguredFormat() {
                return "HH:mm:ss dd.MM.yyyy";
              }

              @Override
              protected String getConfiguredHeaderText() {
                return TEXTS.get("Received");
              }

              @Override
              protected boolean getConfiguredSortAscending() {
                return false;
              }

              @Override
              protected int getConfiguredSortIndex() {
                return 1;
              }

              @Override
              protected int getConfiguredWidth() {
                return 120;
              }
            }

            @Order(40.0)
            public class QoSColumn extends AbstractIntegerColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return TEXTS.get("QoS");
              }

              @Override
              protected int getConfiguredWidth() {
                return 40;
              }
            }

            @Order(50.0)
            public class RetainedColumn extends AbstractBooleanColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return TEXTS.get("Retained");
              }
            }

            @Order(20.0)
            public class ClearMessageLogMenu extends AbstractExtensibleMenu {

              @Override
              protected Set<? extends IMenuType> getConfiguredMenuTypes() {
                return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace, TableMenuType.MultiSelection, TableMenuType.SingleSelection, ValueFieldMenuType.NotNull, TreeMenuType.EmptySpace, TreeMenuType.MultiSelection, TreeMenuType.SingleSelection);
              }

              @Override
              protected String getConfiguredText() {
                return TEXTS.get("ClearMessageLog");
              }

              @Override
              protected void execAction() throws ProcessingException {
                getTable().deleteAllRows();
              }
            }

            @Order(10.0)
            public class ReplyMenu extends AbstractExtensibleMenu {

              @Override
              protected String getConfiguredText() {
                return TEXTS.get("Reply");
              }

              @Override
              protected void execAction() throws ProcessingException {
                ITableRow selectedRow = getTable().getSelectedRow();
                String message = getTable().getMessageColumn().getValue(selectedRow);
                String topic = getTable().getTopicColumn().getValue(selectedRow);

                getMessageField().setValue(TEXTS.get("ReplyPrefix") + message);
                getTopicField().setValue(topic);
              }
            }
          }
        }

        @Order(10.0)
        public class PublishBox extends AbstractSequenceBox {

          @Override
          protected boolean getConfiguredAutoCheckFromTo() {
            return false;
          }

          @Override
          protected boolean getConfiguredLabelVisible() {
            return false;
          }

          @Order(10.0)
          public class MessageField extends AbstractStringField {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Message");
            }

            @Override
            protected int getConfiguredLabelPosition() {
              return LABEL_POSITION_ON_FIELD;
            }

            @Override
            protected void execChangedValue() throws ProcessingException {
              getPublishButton().setEnabled(!StringUtility.isNullOrEmpty(getValue()));
            }
          }

          @Order(20.0)
          public class TopicField extends AbstractStringField {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Topic");
            }

            @Override
            protected int getConfiguredLabelPosition() {
              return LABEL_POSITION_ON_FIELD;
            }
          }

          @Order(30.0)
          public class PublishButton extends AbstractButton {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Publish");
            }

            @Override
            protected void execClickAction() throws ProcessingException {
              AbstractStringField topic = getTopicField();
              AbstractStringField message = getMessageField();
              AbstractIntegerField qos = getDefaultQoSField();
              AbstractBooleanField retained = getDefaultRetainedField();

              // use default topic if the publish topic is empty
              if (StringUtility.isNullOrEmpty(topic.getValue())) {
                topic.setValue(getDefaultTopicField().getValue());
              }

              if (publishFieldsAreValid(topic, message, qos)) {
                getMqttService().publish(
                    topic.getValue(),
                    message.getValue(),
                    qos.getValue(),
                    retained.getValue()
                    );
              }
            }
          }
        }

        @Order(10.0)
        public class EnterKeyStroke extends AbstractKeyStroke {

          @Override
          protected String getConfiguredKeyStroke() {
            return "ENTER";
          }

          @Override
          protected void execAction() throws ProcessingException {
            if (getPublishButton().isEnabled()) {
              getPublishButton().execClickAction();
            }
          }
        }
      }

      @Order(70.0)
      public class SubscriptionsBox extends AbstractGroupBox {

        @Override
        protected int getConfiguredGridColumnCount() {
          return 1;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Subscriptions");
        }

        @Order(10.0)
        public class SubscribeBox extends AbstractSequenceBox {

          @Override
          protected boolean getConfiguredAutoCheckFromTo() {
            return false;
          }

          @Override
          protected boolean getConfiguredLabelVisible() {
            return false;
          }

          @Override
          protected void execInitField() throws ProcessingException {
            getPublishButton().setEnabled(false);
          }

          @Order(10.0)
          public class TopicFilterField extends AbstractStringField {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("TopicFilter");
            }

            @Override
            protected int getConfiguredLabelPosition() {
              return LABEL_POSITION_ON_FIELD;
            }

            @Override
            protected void execChangedValue() throws ProcessingException {
              String topic = getValue();

              if (StringUtility.isNullOrEmpty(topic)) {
                getSubscribeButton().setEnabled(false);
                getUnsubscribeButton().setEnabled(false);
                return;
              }

              Iterator<ITableRow> topics = getSubscriptionsField().getTable().getRows().iterator();
              boolean subscribedTopic = false;

              while (topics.hasNext()) {
                ITableRow row = topics.next();

                String rowTopic = (String) row.getKeyValues().get(0);
                Boolean active = (Boolean) row.getCell(2).getValue();

                if (rowTopic.equals(topic) && active) {
                  subscribedTopic = true;
                  break;
                }
              }

              getSubscribeButton().setEnabled(!subscribedTopic);
              getUnsubscribeButton().setEnabled(subscribedTopic);
            }
          }

          @Order(20.0)
          public class TopicQoSField extends AbstractIntegerField {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("QoS");
            }

            @Override
            protected int getConfiguredLabelPosition() {
              return LABEL_POSITION_ON_FIELD;
            }

            @Override
            protected Integer getConfiguredMaxValue() {
              return 2;
            }

            @Override
            protected Integer getConfiguredMinValue() {
              return 0;
            }
          }

          @Order(30.0)
          public class SubscribeButton extends AbstractButton {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Subscribe");
            }

            @Override
            protected void execClickAction() throws ProcessingException {
              String topic = getTopicFilterField().getValue();
              Integer qos = getTopicQoSField().getValue();

              getMqttService().subscribe(topic, qos);
              getSubscribeButton().setEnabled(false);
              getUnsubscribeButton().setEnabled(true);
              addTopicRow(topic, qos, true);
            }
          }

          @Order(40.0)
          public class UnsubscribeButton extends AbstractButton {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Unsubscribe");
            }

            @Override
            protected void execClickAction() throws ProcessingException {
              String topic = getTopicFilterField().getValue();

              getMqttService().unsubscribe(topic);
              getSubscribeButton().setEnabled(true);
              getUnsubscribeButton().setEnabled(false);

              setTopicRowActiveFlag(topic, false);
            }
          }
        }

        @Order(40.0)
        public class SubscriptionsField extends AbstractTableField<SubscriptionsField.Table> {

          @Override
          protected int getConfiguredGridH() {
            return 7;
          }

          @Override
          protected boolean getConfiguredLabelVisible() {
            return false;
          }

          public void setTopicRowsActiveFlag(boolean active) {
            try {
              getTable().getActiveColumn().fill(active);
            }
            catch (ProcessingException e) {
              // nop
            }
          }

          @Order(10.0)
          public class Table extends AbstractExtensibleTable {

            @Override
            protected void execRowsSelected(List<? extends ITableRow> rows) throws ProcessingException {
              if (rows.size() == 1) {
                getTopicFilterField().setValue((String) rows.get(0).getCellValue(0));
                getTopicQoSField().setValue((Integer) rows.get(0).getCellValue(1));
              }
              else {
                getTopicFilterField().setValue(null);
                getTopicQoSField().setValue(null);
              }
            }

            /**
             * @return the ActiveColumn
             */
            public ActiveColumn getActiveColumn() {
              return getColumnSet().getColumnByClass(ActiveColumn.class);
            }

            /**
             * @return the QoSColumn
             */
            public QoSColumn getQoSColumn() {
              return getColumnSet().getColumnByClass(QoSColumn.class);
            }

            /**
             * @return the TopicFilterColumn
             */
            public TopicFilterColumn getTopicFilterColumn() {
              return getColumnSet().getColumnByClass(TopicFilterColumn.class);
            }

            @Order(10.0)
            public class TopicFilterColumn extends AbstractStringColumn {

              @Override
              protected boolean getConfiguredAutoOptimizeWidth() {
                return true;
              }

              @Override
              protected String getConfiguredHeaderText() {
                return TEXTS.get("TopicFilter");
              }

              @Override
              protected boolean getConfiguredPrimaryKey() {
                return true;
              }

              @Override
              protected int getConfiguredWidth() {
                return 350;
              }
            }

            @Order(20.0)
            public class QoSColumn extends AbstractIntegerColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return TEXTS.get("QoS");
              }
            }

            @Order(30.0)
            public class ActiveColumn extends AbstractBooleanColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return TEXTS.get("Active");
              }
            }

            @Order(10.0)
            public class UnsubscribeMenu extends AbstractExtensibleMenu {

              @Override
              protected String getConfiguredText() {
                return TEXTS.get("Unsubscribe");
              }

              @Override
              protected void execAction() throws ProcessingException {
                // TODO update subscribe/unsubscribe buttons
                getUnsubscribeButton().execClickAction();
              }
            }

            @Order(20.0)
            public class SubscribeMenu extends AbstractExtensibleMenu {

              @Override
              protected String getConfiguredText() {
                return TEXTS.get("Subscribe");
              }

              @Override
              protected void execAction() throws ProcessingException {
                for (ITableRow row : getSelectedRows()) {
                  Cell cell = row.getCellForUpdate(2);
                  Boolean active = (Boolean) cell.getValue();

                  if (!active) {
                    String topic = (String) row.getCellForUpdate(0).getValue();
                    Integer qos = (Integer) row.getCellForUpdate(1).getValue();
                    getMqttService().subscribe(topic, qos);
                    cell.setValue(true);
                  }
                }
              }
            }
          }
        }

        @Order(10.0)
        public class EnterKeyStroke extends AbstractKeyStroke {

          @Override
          protected String getConfiguredKeyStroke() {
            return "ENTER";
          }

          @Override
          protected void execAction() throws ProcessingException {
            if (getSubscribeButton().isEnabled()) {
              getSubscribeButton().execClickAction();
            }
          }
        }
      }
    }
  }

  private ITableRow getTopicRow(String topic) {
    List<String> topicKey = new ArrayList<>();
    ITableRow row = null;

    topicKey.add(topic);
    row = getSubscriptionsField().getTable().findRowByKey(topicKey);

    return row;
  }

  public void addTopicRow(String topic, Integer qos, Boolean active) throws ProcessingException {
    ITableRow row = getTopicRow(topic);

    if (row == null) {
      getSubscriptionsField().getTable().addRowByArray(new Object[]{topic, qos, active});
    }
  }

  private void removeTopicRow(String topic) throws ProcessingException {
    ITableRow row = getTopicRow(topic);

    if (row != null) {
      getSubscriptionsField().getTable().deleteRow(row);
    }
  }

  private void setTopicRowActiveFlag(String topic, boolean active) throws ProcessingException {
    ITableRow row = getTopicRow(topic);

    if (row != null) {
      getSubscriptionsField().getTable().getActiveColumn().setValue(row, active);
    }
  }

  /**
   * validate fields necessary to publish a message.
   * sets/clears error stati accordingly.
   *
   * @return true: field content ok to publish a message, false otherwise
   */
  private boolean publishFieldsAreValid(AbstractStringField message, AbstractStringField topic) {
    return publishFieldsAreValid(message, topic, null);
  }

  private boolean publishFieldsAreValid(
      AbstractStringField topic,
      AbstractStringField message,
      AbstractIntegerField qos)
  {
    boolean valid = true;

    if (StringUtility.isNullOrEmpty(message.getValue())) {
      message.setErrorStatus(TEXTS.get("MessageIsEmpty"));
      valid = false;
    }
    else {
      message.clearErrorStatus();
    }

    if (StringUtility.isNullOrEmpty(topic.getValue())) {
      topic.setErrorStatus(TEXTS.get("TopicIsEmpty"));
      valid = false;
    }
    else {
      topic.clearErrorStatus();
    }

    if (qos != null && qos.getErrorStatus() != null) {
      valid = false;
    }

    return valid;
  }

  private MqttService getMqttService() {
    MqttService service = SERVICES.getService(MqttService.class);
    return service;
  }

  public class ViewHandler extends AbstractFormHandler {
  }
}
