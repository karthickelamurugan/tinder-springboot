import React, { useState } from 'react'

export default function Login({ onLogin }){
  const [username,setUsername] = useState('alice')
  const [password,setPassword] = useState('password')
  const [msg,setMsg] = useState('')

  async function login(){
    setMsg('');
    try{
      const res = await fetch('/api/auth/login', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ username, password }) });
      const j = await res.json();
      if (!res.ok) { setMsg(JSON.stringify(j)); return }
      onLogin(j.token);
    }catch(e){ setMsg(String(e)); }
  }

  return (
    <div style={{maxWidth:400}}>
      <div><label>Username <input value={username} onChange={e=>setUsername(e.target.value)} /></label></div>
      <div><label>Password <input type="password" value={password} onChange={e=>setPassword(e.target.value)} /></label></div>
      <div><button onClick={login}>Login</button></div>
      <div style={{color:'red'}}>{msg}</div>
    </div>
  )
}
