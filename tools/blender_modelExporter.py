#Made by Elias Arno "Jonnelafin" Eskelinen
#Licensed under the MIT Licence
#Open and run this file inside blender's text editor
import bpy, bmesh
obj = bpy.context.active_object
scn = bpy.context.scene

frame = bpy.context.scene.frame_current
end = scn.frame_end
frames = range(end)

#Flags
keyframes = True
filename = "default"
filetype = ".pb3d"

if not keyframes:
    frames = [frame]
verts = []
lines = []
faces = []


pr = ""
t = 0

def oops(self, context):
    global pr
    self.layout.label(text="Processing animation frames" + pr + "...")



def func_object_duplicate_flatten_modifiers(context, ob):
    depth = bpy.context.evaluated_depsgraph_get()
    eobj = ob.evaluated_get(depth)
    mesh = bpy.data.meshes.new_from_object(eobj)
    name = ob.name + "_clean"
    new_object = bpy.data.objects.new(name, mesh)
    new_object.data = mesh
    bpy.context.collection.objects.link(new_object)
    return new_object

def updateMesh():
    global verts
    global lines
    global faces
    global filename
    
    filename = obj.name
    
    #new_obj = obj.copy()
    #new_obj.data = obj.data.copy()
    #new_obj.animation_data_clear()
    #bpy.data.scenes[0].objects.link(new_obj)
    
    
    new_obj = func_object_duplicate_flatten_modifiers(bpy.context, obj)
    
    if obj.mode == 'EDIT':
        # this works only in edit mode,
        bm = bmesh.from_edit_mesh(new_obj.data)
        verts = [vert.co for vert in bm.verts]
        lines = [edge.vertices for edge in bm.edges]
        faces = [face.vertices for edge in bm.polygons]
    
    else:
        # this works only in object mode,
        verts = [vert.co for vert in new_obj.data.vertices]
        lines = [edge.vertices for edge in new_obj.data.edges]
        faces = [edge.vertices for edge in new_obj.data.polygons]
    # coordinates as tuples
    plain_verts = [vert.to_tuple() for vert in verts]
    #Delete the new_obj
    bpy.ops.object.select_all(action='DESELECT')
    new_obj.select_set(True) # Blender 2.8x
    bpy.ops.object.delete() 
#deselectA()

def exp(self):
    global verts
    global lines
    global faces
    global filename
    global obj, scn, frame, end, frames
    obj = bpy.context.active_object
    scn = bpy.context.scene

    frame = bpy.context.scene.frame_current
    end = scn.frame_end
    frames = range(end)
    updateMesh()
    #print(plain_verts)
    st = ""
    st2 = ""
    st3 = ""
    print("Reading vertices...",end="")
    for t in frames:
        pr = "" + str((len(frames)/100*t)) + "%"
        print("Processing frame " + str(t) + "/" + str(len(frames)) + "[" + pr + "]...")
        self.report({'INFO'}, "Processing frame " + str(t) + "/" + str(len(frames)) + "[" + pr + "]...")
        #bpy.context.window_manager.popup_menu(oops, title="Exporting model...",  icon='FILE_TEXT')
        bpy.context.scene.frame_set(t)
        updateMesh()
        st = st + "\n#" + str(t) + "\n"
        for i in verts:
            for c in i:
                #print(int(c*100),end=" ")
                st = st + str(int(c*100)) + " "
            #print()
            st = st + "\n"
        st = st + "\n"
    print("DONE")
    self.report({'INFO'}, "DONE")
    print("Reading edges...",end="")
    self.report({'INFO'}, "Reading edges...")
    for i in lines:
        #print("LINE: " + str(i))
        p1 = i[0]
        p2 = i[1]
        st2 = st2 + str(p1) + " " + str(p2) + " "
        st2 = st2 + "\n"
    st2 = st2 + "\n"
    print("DONE")
    self.report({'INFO'}, "DONE")
    print("Reading faces...",end="")
    self.report({'INFO'}, "Reading faces...")
    for i in faces:
        #print("LINE: " + str(i))
        p1 = i[0]
        p2 = i[1]
        p3 = i[2]
        st3 = st3 + str(p1) + " " + str(p2) + " " + str(p3) + " "
        st3 = st3 + "\n"
    st3 = st3 + "\n"
    print("DONE")
    self.report({'INFO'}, "DONE")
    print("Saving...")
    self.report({'INFO'}, "Saving...")
    f = bpy.data.texts.new(filename + filetype)
    f.from_string(st)
    f = bpy.data.texts.new(filename + "_lines" + filetype)
    f.from_string(st2)
    f = bpy.data.texts.new(filename + "_faces" + filetype)
    f.from_string(st3)
    print("Everything done, have a good day.")
    self.report({'INFO'}, "Done, Have A Good Day!")
def init():
    updateMesh()
    #print(plain_verts)
    
    print("Reading vertices...",end="")
st = ""
st2 = ""
st3 = ""
def step(self):
    global verts
    global lines
    global faces
    global filename
    global t
    global st, st2, st3
    global done
    pr = "" + str((len(frames)/100*t)) + "%"
    print("Processing frame " + str(t) + "/" + str(len(frames)) + "[" + pr + "]...")
    self.report({'INFO'}, "Processing frame " + str(t) + "/" + str(len(frames)) + "[" + pr + "]...")
    #bpy.context.window_manager.popup_menu(oops, title="Exporting model...",  icon='FILE_TEXT')
    bpy.context.scene.frame_set(t)
    updateMesh()
    st = st + "\n#" + str(t) + "\n"
    for i in verts:
        for c in i:
            #print(int(c*100),end=" ")
            st = st + str(int(c*100)) + " "
        #print()
        st = st + "\n"
    st = st + "\n"
    if t + 1 < len(frames):
        t = t + 1
    else:
        done = True
def end():
    global verts
    global lines
    global faces
    global filename
    global t
    global st, st2, st3
    print("Reading edges...",end="")
    for i in lines:
        #print("LINE: " + str(i))
        p1 = i[0]
        p2 = i[1]
        st2 = st2 + str(p1) + " " + str(p2) + " "
        st2 = st2 + "\n"
    st2 = st2 + "\n"
    print("DONE")
    print("Reading faces...",end="")
    for i in faces:
        #print("LINE: " + str(i))
        p1 = i[0]
        p2 = i[1]
        p3 = i[2]
        st3 = st3 + str(p1) + " " + str(p2) + " " + str(p3) + " "
        st3 = st3 + "\n"
    st3 = st3 + "\n"
    print("DONE")
    print("Saving...")
    f = bpy.data.texts.new(filename + filetype)
    f.from_string(st)
    f = bpy.data.texts.new(filename + "_lines" + filetype)
    f.from_string(st2)
    f = bpy.data.texts.new(filename + "_faces" + filetype)
    f.from_string(st3)
    print("Everything done, have a good day.")
done = False
class exporter(bpy.types.Operator):
    """Tooltip"""
    bl_idname = "object.pb_3dexporter"
    bl_label = "Export PB3D"
    
    global done
    def modal(self, context, event):
        if done:
            end()
            return {"FINISHED"}
        if event.type in {'RIGHTMOUSE', 'ESC'}:
            self.cancel(context)
            return {'CANCELLED'}
        if event.type == 'TIMER':
            step(self)
        return {'PASS_THROUGH'}
    @classmethod
    def poll(cls, context):
        return context.active_object is not None

    def execute(self, context):
        wm = context.window_manager
        self._timer = wm.event_timer_add(1, window=context.window)
        wm.modal_handler_add(self)
        #exp(self)
        return {'RUNNING_MODAL'}
        
def register():
    
    bpy.utils.register_class(exporter)
    print("PB3D Exporter registered.")
def unregister():
    bpy.utils.unregister_class(exporter)
    print("PB3D Exporter unregistered.")
if __name__ == "__main__":
    register()