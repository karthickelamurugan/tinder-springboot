import React, { useState } from 'react'
import Login from './Login'
import Profiles from './Profiles'

export default function App(){
  const [token, setToken] = useState(localStorage.getItem('tm_token'))
  return (
    <div style={{padding:20,fontFamily:'Arial'}}>
      <h1>Tinder Mock (React)</h1>
      {!token ? <Login onLogin={(t)=>{setToken(t); localStorage.setItem('tm_token', t)}} /> : <Profiles token={token} onLogout={()=>{setToken(null); localStorage.removeItem('tm_token')}} />}
    </div>
  )
}
