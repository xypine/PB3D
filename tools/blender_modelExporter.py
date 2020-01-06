#Made by Elias Arno "Jonnelafin" Eskelinen
#Licensed under the MIT Licence
#Open and run this file inside blender's text editor
import bpy, bmesh
obj = bpy.context.active_object

if obj.mode == 'EDIT':
    # this works only in edit mode,
    bm = bmesh.from_edit_mesh(obj.data)
    verts = [vert.co for vert in bm.verts]
    lines = [edge.vertices for edge in bm.edges]
    faces = [face.vertices for edge in bm.polygons]

else:
    # this works only in object mode,
    verts = [vert.co for vert in obj.data.vertices]
    lines = [edge.vertices for edge in obj.data.edges]
    faces = [edge.vertices for edge in obj.data.polygons]
# coordinates as tuples
plain_verts = [vert.to_tuple() for vert in verts]



#print(plain_verts)
st = ""
st2 = ""
st3 = ""
print("Reading vertices...",end="")
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
