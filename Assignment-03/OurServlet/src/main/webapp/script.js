function connect_to_servlet()
{
	let conn_str = "ws://localhost:4242";
	let socket = new WebSocket(conn_str);

	socket.onopen = function()
	{
		console.log("connected to " + conn_str);
	};

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

