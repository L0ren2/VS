function connect_to_servlet()
{
	let conn_str = "ws://localhost:4269";// + document.getElementById("ip").value + ":" + document.getElementById("port").value;
	console.log(conn_str);
	let socket = new WebSocket(conn_str);

	socket.onopen = function(e)
	{
		console.log("connected to " + conn_str);
	};

	// weiterleiten zur neuen html page, wo die infos angezeigt werden
	window.location.href = "messenger.html";

	check_for_new_msgs(socket);
}

function check_for_new_msgs(socket)
{
	while(true)
	{
		socket.onmessage = function(e)
		{
			console.log(e.data);
			//TODO more stuff
		};
	}
}

