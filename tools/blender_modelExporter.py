#Made by Elias Arno "Jonnelafin" Eskelinen
#Licensed under the MIT Licence
#Open and run this file inside blender's text editor

bl_info = {
    "name": "PB3D Exporter",
    "description": "Exports the selected object into an .pb3d",
    "author": "Elias Eskelinen aka Jonnelafin",
    "version": (1, 1),
    "blender": (2, 80, 0),
    "location": "Object Properties > Export to .PB3D",
    "warning": "", # used for warning icon and text in addons panel
    "wiki_url": "https://github.com/jonnelafin/PB3D",
    "tracker_url": "https://developer.blender.org",
    "support": "COMMUNITY",
    "category": "Import-Export",
}

import bpy, bmesh
#obj = bpy.context.active_object
obj = ""
#scn = bpy.context.scene
scn = ""

#frame = bpy.context.scene.frame_current
frame = 0
#end = scn.frame_end
end = 1
frames = range(end)

from bpy.props import (StringProperty,
                       BoolProperty,
                       IntProperty,
                       FloatProperty,
                       FloatVectorProperty,
                       EnumProperty,
                       PointerProperty,
                       )
from bpy.types import (Panel,
                       Menu,
                       Operator,
                       PropertyGroup,
                       )

#Flags
keyframes = True
filename = "default"
filetype = ".pb3d"
useExternalOut = False

#AXIS
#up_z = [0, 0, 0]
#up_y = [-90, 0, 0]
#up_y = [0, -90, 0]
up_z = [0, 0, 0]
up_y = [-90, 0, 0]
up_y = [0, -90, 0]
axis = [0, 0, 0]

if not keyframes:
    frames = [frame]
verts = []
lines = []
faces = []
colors = []

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
    global colors
    #filename = obj.name
    
    #new_obj = obj.copy()
    #new_obj.data = obj.data.copy()
    #new_obj.animation_data_clear()
    #bpy.data.scenes[0].objects.link(new_obj)
    
    
    new_obj = func_object_duplicate_flatten_modifiers(bpy.context, obj)
    mesh = new_obj.data
    if not mesh.vertex_colors:
        new_obj.vertex_colors.new()
    mesh = new_obj.data
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
    
    #GET COLORS
    doneC = []
    for polygon in mesh.polygons:
        for i, index in enumerate(polygon.vertices):
            if(not index in doneC):
                loop_index = polygon.loop_indices[i]
                colors.append(mesh.vertex_colors.active.data[loop_index].color)
                print("color: " + str(i) + str(mesh.vertex_colors.active.data[loop_index].color) + " | " + str(index))
                doneC.append(index)
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
def end_func(self):
    global verts
    global lines
    global faces
    global filename
    global t
    global st, st2, st3
    global useExternalOut
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
    print("Saving...")
    self.report({'INFO'}, "Saving......")
    print("Reading vertex color info...",end="")
    self.report({'INFO'}, "Reading vertex color info...")
    st4 = ""
    for i in colors:
        #print("Color: " + str(i))
        r = i[0]
        g = i[1]
        b = i[2]
        st4 = st4 + str(r) + " " + str(g) + " " + str(b) + " "
        st4 = st4 + "\n"
    st4 = st4 + "\n"
    print("DONE")
    if useExternalOut:
        fn = filename.replace(".pb3d", "")
        f = open(fn + filetype,"w")
        f.write(st)
        f.close()
        f = open(fn + "_lines" + filetype, "w")
        f.write(st2)
        f.close()
        f = open(fn + "_faces" + filetype, "w")
        f.write(st3)
        f.close()
        f = open(fn + "_color" + filetype, "w")
        f.write(st4)
        f.close()
    if not useExternalOut:
        f = bpy.data.texts.new(filename + filetype)
        f.from_string(st)
        f = bpy.data.texts.new(filename + "_lines" + filetype)
        f.from_string(st2)
        f = bpy.data.texts.new(filename + "_faces" + filetype)
        f.from_string(st3)
        f = bpy.data.texts.new(filename + "_color" + filetype)
        f.from_string(st4)
    print("Everything done, have a good day.")
    self.report({'INFO'}, "Everything done, have a good day.")
done = False
class exporter(bpy.types.Operator):
    """Click to export selected object in to .pb3d"""
    bl_idname = "object.pb_3d_exporter"
    bl_label = "Export PB3D"
    
    global done
    def modal(self, context, event):
        if done:
            end_func(self)
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
        
        #RESET
        #---
        global obj, scn, frame, end, frames, keyframes, verts, lines, faces, pr, t
        obj = bpy.context.active_object
        scn = bpy.context.scene

        frame = bpy.context.scene.frame_current
        end = scn.frame_end
        frames = range(end)

        if not keyframes:
            frames = [frame]
        verts = []
        lines = []
        faces = []


        pr = ""
        t = 0
        #---
        wm = context.window_manager
        self._timer = wm.event_timer_add(1, window=context.window)
        wm.modal_handler_add(self)
        #exp(self)
        return {'RUNNING_MODAL'}
class MyProperties(PropertyGroup):
    global up_z, up_y, up_x
    my_bool: BoolProperty(
        name="Use external location",
        description="If unchecked, the files will be found in the scripts tab in blender",
        default = True
        )

    my_int: IntProperty(
        name = "Int Value",
        description="A integer property",
        default = 23,
        min = 10,
        max = 100
        )

    my_float: FloatProperty(
        name = "Float Value",
        description = "A float property",
        default = 23.7,
        min = 0.01,
        max = 30.0
        )

    my_float_vector: FloatVectorProperty(
        name = "Float Vector Value",
        description="Something",
        default=(0.0, 0.0, 0.0), 
        min= 0.0, # float
        max = 0.1
    ) 

    my_string: StringProperty(
        name="File Name",
        description=":",
        default="",
        maxlen=1024,
        )

    my_path: StringProperty(
        name = "Export Dir",
        description="Choose a directory:",
        default="",
        maxlen=1024,
        subtype='DIR_PATH'
        )
        
    my_enum: EnumProperty(
        name="Up axis",
        description="Apply Data to attribute.",
        items=[ ("z", "Z", ""),
                ("y", "Y", ""),
                ("x", "X", ""),
               ]
        )

class pbPanel(bpy.types.Panel):
    bl_idname = "pbui"
    bl_label = "Export to .PB3D"
    bl_space_type = 'PROPERTIES'
    bl_region_type = 'WINDOW'
    bl_context = "object"
    def draw(self, context):
        global filename, useExternalOut, up_z, up_y, up_x
        scene = context.scene
        my_tool = scene.my_tool
        
        #self.layout.label(text="You can export to .pb3d here:")
        self.layout.prop(my_tool, "my_path")
        self.layout.prop(my_tool, "my_string")
        #self.layout.prop(my_tool, "my_enum")
        
        self.layout.prop(my_tool, "my_bool")
        self.layout.operator("object.pb_3d_exporter")
        self.layout.label(text="You can always cancel exporting by pressing esc or mouse 2")
        self.layout.label(text="Animation range will be set to scene frame range")
        useExternalOut = bpy.context.scene.my_tool.my_bool
        filename = bpy.context.scene.my_tool.my_path + "/" + bpy.context.scene.my_tool.my_string
        
        ax = bpy.context.scene.my_tool.my_enum
        #if(ax == 'z'):
        #    axis = up_z
        #if(ax == 'y'):
        #    axis = up_y
        #if(ax == 'x'):
        #    axis = up_x
def register():
    bpy.utils.register_class(exporter)
    bpy.utils.register_class(pbPanel)
    bpy.utils.register_class(MyProperties)
    
    bpy.types.Scene.my_tool = PointerProperty(type=MyProperties)
    print("PB3D Exporter registered.")
def unregister():
#    bpy.utils.unregister_class(exporter)
    bpy.utils.unregister_class(exporter)
    bpy.utils.unregister_class(pbPanel)
    bpy.utils.unregister_class(MyProperties)
    print("PB3D Exporter unregistered.")
if __name__ == "__main__":
    register()