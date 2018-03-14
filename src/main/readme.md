#High level overview of EUX Architecture
#Main Code Chunks
-	Web (or other) GUI
-	Core Server
-	Core API
-	Shared Libraries
-	Individual Backend Interfaces

##Web GUI Functionality
-	Single-page web application
-	Current thought is to build on vue.js, d3 graphing, etc?  TBD 
-	TBS: Widget library (Compare SyncFusion, etc)
-	UI layer subscribes to Data via Core Server REST requests, gets updates pushed via WebSockets, falls back to REST polling if sockets fail.
-	
##Core API
-	Data Node definitions
o	Label(s)
o	Tree structure
o	Data type
o	Value(s)
o	Support for lists, files, etc.
o	TBS: Change detection for individual node?
-	Server to Backend Interface API
o	Initialization / Setup.   Include callback information for Interfaces  to provide Data to Core
o	Update data needs (Add / Remove / Set / Clear?)
o	Shutdown?
o	TBS: Control Messages?  Push data from GUI?  
##Core Server Major Functionality
-	Authentication and Authorization: Users authenticated against (LDAP/AD/etc). User session created.  Data access individually authorized based on user credentials and label on data.  Label is treated as part of data, so delta engine will flag changes for re-evaluation. 
-	Individual server (TFE, CCT, etc) built by specification of which individual backend interfaces are needed at build-time, along with whatever configuration is needed for those interfaces.
-	Data is accessible via individual node or group.  Hierarchical label associated with data.  Data is provided by individual backend interfaces.  Also may need some metadata about user rights, system status.
-	Main interface to GUI is via REST web services for control, WebSockets push for data updates.  GUI can fall back to REST requests for data.  TBS other interfaces for non-web applications.
-	Accepts requests from individual session for data (group/nodes) (TBS: how is data identified?), GUI may also hint/specify update rate for data when using WebSockets.
-	Consolidates data needs from all sessions, informs backend interfaces of needs, and waits for data updates from them.  Maintains tree of current values for data currently needed, performs change detection on updates from backend interfaces, propagates changes based on update rate from GUI, or places the results in a buffer for REST polling if necessary.
##Shared Library
-	Core delta engine for determining changed nodes between 2 versions of a data set / tree.
-	Statistics engine (mean, stddev, etc) on Data Nodes
-	Standard Data Node EU conversions
-	Security label functions (copy, compare, consolidate multiple, etc)
-	etc.
##Individual Backend Interface
-	Start with a Custom Interface for each Service (TFE, CCT, Database, etc). Potential future interface could be shared for all Braxton Backends, with configuration file describing Data, Control, etc.
-	Retrieve data from services (CCT , TFE), or listen for messages from same.  Use shared delta library if necessary to determine changes.
-	Using deltas and list of needs from Core, perform calculations on data and present to Core via callback
-	Maintain security labeling on data as calculations are performed.
-	Translate control / data update messages and send to Backend as needed
