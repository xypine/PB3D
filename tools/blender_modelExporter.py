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

else:
    # this works only in object mode,
    verts = [vert.co for vert in obj.data.vertices]
    lines = [edge.vertices for edge in obj.data.edges]

# coordinates as tuples
plain_verts = [vert.to_tuple() for vert in verts]



#print(plain_verts)
st = ""
st2 = ""
for i in verts:
    for c in i:
        print(int(c*100),end=" ")
        st = st + str(int(c*100)) + " "
    print()
    st = st + "\n"
for i in lines:
    print("LINE: " + str(i))
    p1 = i[0]
    p2 = i[1]
    print(str(obj.data.vertices[p1].co) + ", " + str(obj.data.vertices[p2].co))
    print(int(obj.data.vertices[p1].co[0]*100),end=" ")
    print(int(obj.data.vertices[p1].co[1]*100),end=" ")
    print(int(obj.data.vertices[p1].co[2]*100),end=" ")
    print(int(obj.data.vertices[p2].co[0]*100),end=" ")
    print(int(obj.data.vertices[p2].co[1]*100),end=" ")
    print(int(obj.data.vertices[p2].co[2]*100),end=" ")
    st2 = st2 + str(int(obj.data.vertices[p1].co[0]*100)) + " "
    st2 = st2 + str(int(obj.data.vertices[p1].co[1]*100)) + " "
    st2 = st2 + str(int(obj.data.vertices[p1].co[2]*100)) + " "
    st2 = st2 + str(int(obj.data.vertices[p2].co[0]*100)) + " "
    st2 = st2 + str(int(obj.data.vertices[p2].co[1]*100)) + " "
    st2 = st2 + str(int(obj.data.vertices[p2].co[2]*100)) + " "
    print()
    st2 = st2 + "\n"
f = bpy.data.texts.new('model.txt')
f.from_string(st)
f = bpy.data.texts.new('model_lines.txt')
f.from_string(st2)
print("Everything is done, have a good day!")