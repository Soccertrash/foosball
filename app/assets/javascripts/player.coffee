#= require Paginator
###
OnLoad
###  
$ -> # document ready
    paginator = new Paginator(jsRoutes.controllers.player.PlayerController.paginatorConfiguration(), jsRoutes.controllers.player.PlayerController)
    paginator.setup() 
    
    WS = if window['MozWebSocket'] then MozWebSocket else WebSocket
    
    baseURL = jsRoutes.controllers.player.PlayerController.listenForUpdates().webSocketURL()
    sock = new WS(baseURL)
            
    sock.onmessage = (event) ->
       paginator.reloadPage()
            
    
    
 