MACHINE Lift

CONSTANTS groundf, topf

PROPERTIES groundf = -1 & topf = 1 

VARIABLES  cur_floor, inside_buttons, door_open, call_buttons, direction_up 

INVARIANT  cur_floor : (groundf..topf) & inside_buttons <: (groundf..topf)  & door_open : BOOL
	& call_buttons <: (groundf..topf) & direction_up : BOOL

INITIALISATION cur_floor := 0 ||
 inside_buttons := {} ||
 door_open := FALSE ||
 call_buttons := {} ||
 direction_up := TRUE

OPERATIONS

	move_up = PRE cur_floor < topf & door_open = FALSE & direction_up = TRUE THEN cur_floor := cur_floor + 1 END ;
	
	move_down = PRE cur_floor > groundf & door_open = FALSE & direction_up = FALSE THEN cur_floor := cur_floor - 1 END ;
	
	reverse_lift_down = PRE direction_up = TRUE THEN direction_up := FALSE END;

	reverse_lift_up = PRE direction_up = FALSE THEN direction_up := TRUE END;

	open_door = PRE door_open = FALSE & cur_floor : (inside_buttons \/ call_buttons) THEN door_open := TRUE END;

	close_door = PRE door_open = TRUE THEN door_open := FALSE || inside_buttons := inside_buttons\{cur_floor} || call_buttons := call_buttons\{cur_floor} END;

	push_inside_button(b) = PRE b : (groundf..topf) & b /: inside_buttons & b /= cur_floor THEN inside_buttons := inside_buttons \/ {b} END;

	push_call_button(b) = PRE  b : (groundf..topf) & b /: call_buttons THEN call_buttons := call_buttons \/ {b} END

END

