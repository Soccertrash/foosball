###
Transformed from Javascript to coffeescript from http://jsfiddle.net/sxGtM/3/
###

$.fn.toJson = ->
    result = {}
    a = this.serializeArray()
    $.each(a, -> 
    	if result[this.name]?
    	    if !result[this.name].push 
    	        result[this.name] = [result[this.name]]
    	    result[this.name].push(this.value || '')
    	else
    	    result[this.name] = this.value || ''
    )
    result

###
Cares about displaying all the errors of a form by iterating
over all the field errors. 
###
$.fn.handleFormError = (jsonError) ->
    keysbyindex = Object.keys(jsonError)
    this.setError i, jsonError[i] for i in keysbyindex
    return

###
Sets the CSS class error to the div which has the class "control-group"
Sets the error message to the appropriate field
###
$.fn.setError = (key, value) ->
    $('div[class="control-group"]:has(#'+key+')',this).addClass('error')
    $('#'+key,this).next('span[class="help-inline"]').text(value)
    return
    
###
Send an AJAX Request to the Backend. 
Hand in the target route (which will be the endpoint). The data is taken from the form
by just calling toJson on the form.
###    
$.fn.sendBackend = (route) -> 
    form = this
    json = JSON.stringify(form.toJson())
    route.ajax(
        cache: false
        dataType: "json"
        data: json
        type: "POST"
        contentType: "application/json"
        error: (data) ->
             form.resetForm(false)
             form.handleFormError(JSON.parse(data.responseText))
        success: (data) ->
        	 form.resetForm(false)
        	 form.displayFormSuccess(data['result'])
        beforeSend: () ->
             $('button[data-loading-text]',form).button('loading')
        complete: () ->
             $('button[data-loading-text]',form).button('reset')
    )
    return

###
Reset a form
###
$.fn.resetForm = (cleanInput) ->
    $(':input',this).not(':button, :submit, :reset, :hidden')
    .val('')
    .removeAttr('checked')
    .removeAttr('selected') if cleanInput? and cleanInput
    $('div',this).removeClass('error');
    $('span[data-error]',this).text('');
    $('span[data-success]',this).text('');
    $('span[data-success]',this).parentsUntil(".control-group.success.hide").parent().slideUp()
    return

###
Display Success message
###
$.fn.displayFormSuccess = (message) -> 
    $('span[data-success]',this).text(message)
    $('span[data-success]',this).parentsUntil(".control-group.success.hide").parent().slideDown()


###
OnLoad
### 
$ -> # document ready
    $('form').submit( ->
        $('form').sendBackend(jsRoutes.controllers.player.PlayerController.add())
        return false
        )
    $('button[class="btn"]').click( ->
    	$(this).parentsUntil('form').parent().resetForm(true)
    )
        
