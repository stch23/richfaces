/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.renderkit;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.component.InplaceComponent;
import org.richfaces.component.InplaceState;
import org.richfaces.component.util.HtmlUtil;

/**
 * @author Anton Belevich
 * 
 */
@ResourceDependencies({
        @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(name = "jquery.js"),
        @ResourceDependency(name = "richfaces.js"),
        @ResourceDependency(name = "richfaces-event.js"),
        @ResourceDependency(name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "inputBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceInput.js"),
        @ResourceDependency(library = "org.richfaces", name = "inplaceInput.ecss") })
public class InplaceInputRendererBase extends InputRendererBase {

    public static final String OPTIONS_EDIT_EVENT = "editEvent";
    
    public static final String OPTIONS_STATE = "state";

    public static final String OPTIONS_EDIT_CONTAINER = "editContainer";

    public static final String OPTIONS_INPUT = "input";

    public static final String OPTIONS_FOCUS = "focusElement";

    public static final String OPTIONS_BUTTON_OK = "okbtn";

    public static final String OPTIONS_LABEL = "label";

    public static final String OPTIONS_DEFAULT_LABEL = "defaultLabel";

    public static final String OPTIONS_BUTTON_CANCEL = "cancelbtn";

    public static final String OPTIONS_SHOWCONTROLS = "showControls";

    public static final String OPTIONS_NONE_CSS = "noneCss";

    public static final String OPTIONS_CHANGED_CSS = "changedCss";
    
    public static final String OPTIONS_EDIT_CSS = "editCss";

    public static final String OPTIONS_INITIAL_VALUE = "initialValue";

    public static final String OPTIONS_SAVE_ON_BLUR = "saveOnBlur";

    protected static final Map<String, ComponentAttribute> INPLACEINPUT_HANDLER_ATTRIBUTES = Collections
            .unmodifiableMap(ComponentAttribute.createMap(
                    new ComponentAttribute(HtmlConstants.ONCLICK_ATTRIBUTE)
                            .setEventNames("inputclick")
                            .setComponentAttributeName("oninputclick"),
                    new ComponentAttribute(HtmlConstants.ONDBLCLICK_ATTRIBUTE)
                            .setEventNames("inputdblclick")
                            .setComponentAttributeName("oninputdblclick"),
                    new ComponentAttribute(HtmlConstants.ONMOUSEDOWN_ATTRIBUTE)
                            .setEventNames("inputmousedown")
                            .setComponentAttributeName("oninputmousedown"),
                    new ComponentAttribute(HtmlConstants.ONMOUSEUP_ATTRIBUTE)
                            .setEventNames("inputmouseup")
                            .setComponentAttributeName("oninputmouseup"),
                    new ComponentAttribute(HtmlConstants.ONMOUSEOVER_ATTRIBUTE)
                            .setEventNames("inputmouseover")
                            .setComponentAttributeName("oninputmouseover"),
                    new ComponentAttribute(HtmlConstants.ONMOUSEMOVE_ATTRIBUTE)
                            .setEventNames("inputmousemove")
                            .setComponentAttributeName("oninputmousemove"),
                    new ComponentAttribute(HtmlConstants.ONMOUSEOUT_ATTRIBUTE)
                            .setEventNames("inputmouseout")
                            .setComponentAttributeName("oninputmouseout"),
                    new ComponentAttribute(HtmlConstants.ONKEYPRESS_ATTRIBUTE)
                            .setEventNames("inputkeypress")
                            .setComponentAttributeName("oninputkeypress"),
                    new ComponentAttribute(HtmlConstants.ONKEYDOWN_ATTRIBUTE)
                            .setEventNames("inputkeydown")
                            .setComponentAttributeName("oninputkeydown"),
                    new ComponentAttribute(HtmlConstants.ONKEYUP_ATTRIBUTE)
                            .setEventNames("inputkeyup")
                            .setComponentAttributeName("oninputkeyup"),
                    new ComponentAttribute(HtmlConstants.ONBLUR_ATTRIBUTE)
                            .setEventNames("inputblur")
                            .setComponentAttributeName("oninputblur"),
                    new ComponentAttribute(HtmlConstants.ONFOCUS_ATTRIBUTE)
                            .setEventNames("inputfocus")
                            .setComponentAttributeName("oninputfocus"),
                    new ComponentAttribute(HtmlConstants.ONCHANGE_ATTRIBUTE)
                            .setEventNames("inputchange").setComponentAttributeName(
                                    "oninputchange"),
                    new ComponentAttribute(HtmlConstants.ONSELECT_ATTRIBUTE)
                            .setEventNames("inputselect").setComponentAttributeName(
                                    "oninputselect")));

    public void renderInputHandlers(FacesContext facesContext,
            UIComponent component) throws IOException {
        RenderKitUtils.renderPassThroughAttributesOptimized(facesContext,
                component, INPLACEINPUT_HANDLER_ATTRIBUTES);
    }

    public InplaceState getInplaceState(UIComponent component) {
        return ((InplaceComponent) component).getState();
    }

    public String getValue(FacesContext facesContext, UIComponent component) throws IOException {
        String value = getInputValue(facesContext, component);
        if (!isDisable(getInplaceState(component)) && (value == null || "".equals(value)) ) {
            value = ((InplaceComponent) component).getDefaultLabel();
        }
        return value;
    }

    public String getResourcePath(FacesContext context, String resourceName) {
        if (resourceName != null) {
            ResourceHandler resourceHandler = context.getApplication()
                    .getResourceHandler();
            Resource resource = resourceHandler.createResource(resourceName);
            return resource.getRequestPath();
        }
        return null;
    }

    public String getStateStyleClass(UIComponent component,
            InplaceState inplaceState) {
        String style = getReadyStateCss();
        switch (inplaceState) {
            case edit:
                style = HtmlUtil.concatClasses(style, getEditStateCss());
                break;
    
            case changed: 
                style = HtmlUtil.concatClasses(style, getChangedStateCss());
                break;
    
            case disable:
                style = getDisableStateCss();
                break;
    
            default:
                break;
        }

        return style;
    }

    public boolean isDisable(InplaceState currentState) {
        return (InplaceState.disable == currentState); 
    }
    
    public String getEditStyleClass(UIComponent component,
            InplaceState inplaceState) {
        return (InplaceState.edit != inplaceState) ? HtmlUtil.concatClasses(getEditCss(), getNoneCss()) : getEditCss();
    }
    
    public void buildScript(ResponseWriter writer, FacesContext facesContext,
            UIComponent component, Object additional) throws IOException {
        if (!(component instanceof InplaceComponent)) {
            return;
        }

        String scriptName = getScriptName();
        JSFunction function = new JSFunction(scriptName);
        String clientId = component.getClientId(facesContext);
        Map<String, Object> options = createInplaceComponentOptions(clientId,
                (InplaceComponent) component);
        addToOptions(facesContext, component, options, additional);
        function.addParameter(clientId);
        function.addParameter(options);
        writer.write(function.toString());
    }

    protected String getScriptName() {
        return "new RichFaces.ui.InplaceInput";
    }

    private Map<String, Object> createInplaceComponentOptions(String clientId,
            InplaceComponent inplaceComponent) {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put(OPTIONS_EDIT_EVENT, inplaceComponent.getEditEvent());
        options.put(OPTIONS_STATE, inplaceComponent.getState());
        options.put(OPTIONS_NONE_CSS, getNoneCss());
        options.put(OPTIONS_CHANGED_CSS, getChangedStateCss());
        options.put(OPTIONS_EDIT_CSS, getEditStateCss());
        options.put(OPTIONS_EDIT_CONTAINER, clientId + "Edit");
        options.put(OPTIONS_INPUT, clientId + "Input");
        options.put(OPTIONS_LABEL, clientId + "Label");
        options.put(OPTIONS_FOCUS, clientId + "Focus");
        options.put(OPTIONS_DEFAULT_LABEL, inplaceComponent.getDefaultLabel());
        options.put(OPTIONS_SAVE_ON_BLUR, inplaceComponent.isSaveOnBlur());

        boolean showControls = inplaceComponent.isShowControls();

        options.put(OPTIONS_SHOWCONTROLS, showControls);
        if (showControls) {
            options.put(OPTIONS_BUTTON_OK, clientId + "Okbtn");
            options.put(OPTIONS_BUTTON_CANCEL, clientId + "Cancelbtn");
        }
        return options;
    }
    
    public void addToOptions(FacesContext facesContext, UIComponent component,
            Map<String, Object> options, Object additional) {
        // override this method if you need additional options
    }

    public String getReadyStateCss() {
        return "rf-ii-d-s";
    }

    public String getEditStateCss() {
        return "rf-ii-e-s";
    }

    public String getChangedStateCss() {
        return "rf-ii-c-s";
    }

    public String getDisableStateCss() {
        return "rf-ii-dis-s";
    }

    public String getEditCss() {
        return "rf-ii-edit";
    }

    public String getNoneCss() {
        return "rf-ii-none";
    }
}
