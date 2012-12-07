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

$.fn.handleFormError = (jsonError) ->
    keysbyindex = Object.keys(jsonError)
    this.setError i, jsonError[i] for i in keysbyindex
    return

$.fn.setError = (key, value) ->
    $('div[class="control-group"]:has(#'+key+')',this).addClass('error')
    $('#'+key,this).next('span[class="help-inline"]').text(value)
    return
    
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
             form.handleFormError(JSON.parse(data.responseText))
             

    )
    return

$.fn.resetForm = ->
    $(':input',this).not(':button, :submit, :reset, :hidden')
    .val('')
    .removeAttr('checked')
    .removeAttr('selected');
    $('div',this).removeClass('error');
    $('span[data-error]',this).text('');
    return
    

###
Display form as JSON
###  
$ -> # document ready
    $('form').submit( ->
        $('form').sendBackend(jsRoutes.controllers.PlayerController.add())
        return false
        )
    $('button[class="btn"]').click( ->
    	$(this).parentsUntil('form').resetForm()
    )
        
