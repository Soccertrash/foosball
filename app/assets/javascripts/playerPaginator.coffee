class Paginator
    constructor: (@config,@dataSource) ->
    
    rowCount = -1
    pageSize = -1
    columns = null
    sortingPropertyName = null
    sortingIsAsc = true 
    currentPage = -1
    
    ###
    Initial setup. Loads the Paginator Config from 
    Backend
    ###
    setup: =>
        @config.ajax(
            cache: false
            type: "GET"
            contentType: "application/json"
            dataType: "json"
            success: (data) =>
                this.configurationReceived(data)
        )
    ###
    Call back method which is called when the configuration has been recevied from 
    backend
    ###  
    configurationReceived: (data) =>
         this.storeConfig(data)
         this.setupGUI()
         this.loadPage(1)
         $('.pagination ul li').click( (event) =>
             this.loadPage($(event.currentTarget).text())
             return false
         )

    ### 
    Store the configuration to instance variables
    ###
    storeConfig: (jsonData) =>
        rowCount = jsonData['rowAmount']
        pageSize = jsonData['pageSize']
        columns = jsonData['columns']
        sortingPropertyName = columns[0].propertyName
        sortingIsAsc = true
        return
        
    ###
    Set up the GUI after initial configuration has been received
    ###    
    setupGUI: () =>
        $('.pagination').append("<ul>")
        $('.pagination ul').append("<li><a href=\"#\">Prev</a></li>")
        amountPages = Math.ceil(rowCount/pageSize)
        $('.pagination ul').append("<li><a href=\"#\">#{i}</a></li>") for i in [1..amountPages] if amountPages >0
        $('.pagination ul').append("<li><a href=\"#\">Next</a></li>")
        $('.pagination').show()
        table = $('.pagination').next()
        table.append("<thead>")
        $('thead',table).append('<tr>')
        for col in columns
            if col.sortable
                $('thead tr',table).append("<th data-propertyName=\"#{col.propertyName}\">#{col.columnHeader}</th>")
                $('thead tr th:last',table).click((event) =>
                    selectedProperty= $(event.currentTarget).attr('data-propertyName')
                    if selectedProperty == sortingPropertyName
                        sortingIsAsc = !sortingIsAsc
                    else
                       sortingPropertyName = selectedProperty
                       sortingIsAsc = true
                    this.loadPage(currentPage)
                    $("thead tr th[data-propertyName] i",table).remove()
                    arrowDirection = if sortingIsAsc then "down" else "up"
                    $("thead tr th[data-propertyName=\"#{sortingPropertyName}\"]",table).append("<i class=\"icon-arrow-#{arrowDirection}\"></i>")
                    return false
                )
            else
                $('thead tr',table).append("<th>#{col.columnHeader}</th>")
        
        arrowDirection = if sortingIsAsc then "down" else "up"
        $("thead tr th[data-propertyName]:eq(0)",table).append("<i class=\"icon-arrow-#{arrowDirection}\"></i>")
                  
     
     ###
     Set CSS for Prev/next
     ###   
     checkStatusOfPrevAndNext: () =>
        currentPageInt = parseInt(currentPage)
        if currentPageInt == 1 
            $('.pagination ul li:eq(0)').addClass('disabled')
        else
            $('.pagination ul li:eq(0)').removeClass('disabled')
        if currentPageInt ==  Math.ceil(rowCount/pageSize) 
            $('.pagination ul li:last').addClass('disabled')
        else
            $('.pagination ul li:last').removeClass('disabled')
            
     	
     
     ###
     Load a certain page from Backend and display it
     ###   
     loadPage: (page) =>
         requestData = "#{sortingPropertyName}=#{sortingIsAsc}"
         @dataSource.list(page).ajax(
            cache: false
            type: "GET"
            contentType: "application/json"
            data: requestData
            dataType: "json"
            success: (data) =>
                this.fillTable(data)
                $('.pagination li.active').removeClass('active')
                $('.pagination li:eq('+page+')').addClass('active')
         )
         currentPage = page
         this.checkStatusOfPrevAndNext()
         
      ###
      Update current page
      ###
      reloadPage: () =>
          this.loadPage(currentPage)
      
      ###
      Fill the actual table rows with the data received from Backend
      ###
      fillTable: (jsonData) =>
          table = $('.pagination').next()
          $('tbody',table).remove() if $('tbody',table)?
          table.append('<tbody></tbody>')
          tbody = $('tbody',table)
          handleRow = (row) ->
              tbody.append('<tr>')
              $('tr:last',tbody).append('<td>'+row[column.propertyName]+'</td>')  for column in columns

           handleRow row for row in jsonData
          

###
OnLoad
###  
$ -> # document ready
    paginator = new Paginator(jsRoutes.controllers.PlayerController.paginatorConfiguration(), jsRoutes.controllers.PlayerController)
    paginator.setup() 
    
    WS = if window['MozWebSocket'] then MozWebSocket else WebSocket
    
    baseURL = jsRoutes.controllers.PlayerController.listenForUpdates().webSocketURL()
    sock = new WS(baseURL)
            
    sock.onmessage = (event) ->
       paginator.reloadPage()
            
    
    
 