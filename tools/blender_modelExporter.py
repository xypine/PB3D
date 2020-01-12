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

if not keyframes:
    frames = [frame]
verts = []
lines = []
faces = []

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

updateMesh()
#print(plain_verts)
st = ""
st2 = ""
st3 = ""
print("Reading vertices...",end="")
for t in frames:
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
f = bpy.data.texts.new('model.txt')
f.from_string(st)
f = bpy.data.texts.new('model_lines.txt')
f.from_string(st2)
f = bpy.data.texts.new('model_faces.txt')
f.from_string(st3)
print("Everything done, have a good day.")



