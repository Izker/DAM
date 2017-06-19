package Interaction;


import ConnectDB.*;
//import Annotation.*;
//import java.lang.annotation.*;
//import java.lang.reflect.*;

public abstract class Query {
	
	public abstract void executeQuery(Session s);
	
}
