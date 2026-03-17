import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./Main.css";

function Dashboard() {
    let user = {};
    try {
        const storedUser = localStorage.getItem("user");
        if (storedUser && storedUser !== "undefined") {
            user = JSON.parse(storedUser);
        }
    } catch (error) {
        console.error("Invalid user in localStorage");
    }

    const [users, setUsers] = useState([]);
    const navigate = useNavigate();
    const token = localStorage.getItem("token");

    useEffect(() => {
        if (token) getAllUsers();
    }, [token]);

    useEffect(() => {
        if (!token) {
            logout();
            return;
        }

        axios
            .get("https://ttdeployment-2t9z.onrender.com/verify", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
            .catch(() => {
                alert("Session expired");
                logout();
            });
    }, [token]);

    async function getAllUsers() {
        try {
            const res = await axios.get("https://ttdeployment-2t9z.onrender.com/allUsers", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setUsers(res.data);
        } catch (err) {
            alert("Error fetching users");
        }
    }

    async function deleteUser(id) {
        try {
            if (window.confirm("Are you sure you want to delete this user?")) {
                await axios.delete(`https://ttdeployment-2t9z.onrender.com/deleteUser/${id}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                alert("User deleted successfully");
                getAllUsers();
            }
        } catch (err) {
            alert("Error deleting user");
        }
    }

    function logout() {
        localStorage.clear();
        navigate("/login");
    }

    async function updateUser(id, oldUser) {
        const newEmail = prompt("Enter new email:", oldUser.email);
        if (newEmail) {
            try {
                await axios.put(
                    `https://ttdeployment-2t9z.onrender.com/updateUser/${id}`,
                    { ...oldUser, email: newEmail },
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    }
                );
                alert("User updated successfully");
                getAllUsers();
            } catch (err) {
                alert("Error updating user");
            }
        }
    }

    return (
        <div>

           <div className="dashboard-header">
    <div></div>
    <h1>Dashboard</h1>
    <button className="logout-btn" onClick={logout}>Logout</button>
</div>

<p>Welcome to your dashboard! {user?.username}</p>



            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map((u) => (
                        <tr key={u.id}>
                            <td>{u.id}</td>
                            <td>{u.username}</td>
                            <td>{u.email}</td>
                            <td>
                                <button onClick={() => deleteUser(u.id)}>Delete</button>
                                <button onClick={() => updateUser(u.id, u)}>Update</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default Dashboard;
