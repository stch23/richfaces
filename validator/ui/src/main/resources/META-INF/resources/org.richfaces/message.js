(function($, rf) {

	rf.ui = rf.ui || {};
	
	// Constructor definition
	rf.ui.Base = function(componentId, options, defaultOptions) {
		this.namespace = "."+rf.Event.createNamespace(this.name, componentId);
		// call constructor of parent class
		$super.constructor.call(this, componentId);
		this.options = $.extend(this.options, defaultOptions, options);
		this.attachToDom();
		this.__bindEventHandlers();
	};

	// Extend component class and add protected methods from parent class to our container
	rf.BaseComponent.extend(rf.ui.Base);

	// define super class link
	var $super = rf.ui.Base.$super;

	$.extend(rf.ui.Base.prototype, {
		__bindEventHandlers: function () {
		},
		destroy: function () {
			rf.Event.unbindById(this.id, this.namespace);
			$super.destroy.call(this);
		}
	});
	
})(jQuery, window.RichFaces || (window.RichFaces={}));

(function($, rf) {
	
	// Constructor definition
	rf.ui.Message = function(componentId, options) {
		// call constructor of parent class
		$super.constructor.call(this, componentId, options, defaultOptions);
	};

	// Extend component class and add protected methods from parent class to our container
	rf.ui.Base.extend(rf.ui.Message);

	// define super class link
	var $super = rf.ui.Message.$super;

	var defaultOptions = {
			showSummary:true,
			level:0
	};
	
	var severetyClasses=["rf-msg-inf","rf-msg-wrn","rf-msg-err","rf-msg-ftl"];
	
	var onMessage = function (event, element, data) {
		var content = $(rf.getDomElement(this.id));
		var sourceId = data.sourceId;
		var message = data.message;
		if (!this.options.forComponentId) {
			if (!message) {
				// rf.csv.clearMessage
				$(rf.getDomElement(this.id+':'+sourceId)).remove();
			} else {
				renderMessage.call(this,sourceId,message);
			}
		} else if (this.options.forComponentId === sourceId) {
			content.empty();
			renderMessage.call(this,sourceId,message);
		}
	}
	
	var renderMessage = function(index,message){
		if(message && message.severity >= this.options.level){
			var content = $(rf.getDomElement(this.id));
			var msgContent = "<span class='"+severetyClasses[message.severity]+"' id='"+this.id+':'+index+"'";
			if(message.summary){
				if(this.options.tooltip){
					msgContent = msgContent+" title='"+message.summary+"'>";
				} else if(this.options.showSummary ){
					msgContent = msgContent + "><span class='rf-msg-sum'>"+message.summary+"</span>";
				} else {
					msgContent = msgContent+">";
				}
			} else {
				msgContent = msgContent+">";
			}
			if(this.options.showDetail && message.detail){
				msgContent = msgContent + "<span class='rf-msg-dtl'>"+message.detail+"</span>";
			}
			msgContent = msgContent+"</span>"
			content.append(msgContent);
		}
	}

	$.extend(rf.ui.Message.prototype, {
		name: "Message",
		__bindEventHandlers: function () {
			rf.Event.bind(window.document, rf.Event.MESSAGE_EVENT_TYPE+this.namespace, onMessage, this);
		}
	});
	
})(jQuery, window.RichFaces || (window.RichFaces={}));